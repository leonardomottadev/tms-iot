using MQTTnet;
using System.Text.Json;

var ClientId = "emqx-publisher-" + Guid.NewGuid();
Console.WriteLine("ClientID: " + ClientId);

var rand = new Random();

string jsonPayload;

var mqttFactory = new MqttClientFactory();

using (var mqttClient = mqttFactory.CreateMqttClient())
{
    var mqttClientOptions = new MqttClientOptionsBuilder()
        .WithTcpServer("broker.emqx.io", 1883)
        .WithClientId(ClientId)
        .WithCleanSession()
        .Build();

    await mqttClient.ConnectAsync(mqttClientOptions, CancellationToken.None);

    try
    {
        while (true)
        {
            var sensorData = new
            {
                temperature = Math.Round(20 + rand.NextDouble() * 10, 2), // 20~30°C
                humidity = Math.Round(40 + rand.NextDouble() * 20, 2),    // 40~60%
                timestamp = TimeZoneInfo
                                .ConvertTimeFromUtc(DateTime.UtcNow, TimeZoneInfo.FindSystemTimeZoneById("E. South America Standard Time"))
                                .ToString("o")
            };

            jsonPayload = JsonSerializer.Serialize(sensorData);

            var applicationMessage = new MqttApplicationMessageBuilder()
                .WithTopic("tms-iot/test/sensor-reader/payload")
                .WithPayload(jsonPayload)
                .WithRetainFlag(false)
                .Build();

            await mqttClient.PublishAsync(applicationMessage, CancellationToken.None);
            Console.WriteLine($"Enviado às {DateTime.Now:T}: {jsonPayload}");

            await Task.Delay(TimeSpan.FromSeconds(10));
        }
    }
    catch (Exception ex)
    {
        Console.WriteLine(ex.ToString());
    }
    finally
    {
        await mqttClient.DisconnectAsync();
    }
}
