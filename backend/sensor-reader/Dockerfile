# STAGE 1: Build com .NET 9 SDK
FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build
WORKDIR /sensor-reader

# Copia todos os arquivos do projeto
COPY . ./

# Restaura pacotes
RUN dotnet restore

# Publica em modo release
RUN dotnet publish -c Release -o out

# STAGE 2: Runtime com ASP.NET (ou use runtime puro se for console app)
FROM mcr.microsoft.com/dotnet/sdk:9.0
WORKDIR /sensor-reader

# Copia arquivos do build
COPY --from=build /sensor-reader/out ./

# Define o ponto de entrada
ENTRYPOINT ["dotnet", "Tms.Backend.SensorReader.dll"]
