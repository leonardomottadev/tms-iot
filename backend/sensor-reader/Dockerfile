FROM mcr.microsoft.com/dotnet/sdk:9.0 AS build

WORKDIR /sensor-reader

COPY . ./

RUN dotnet restore

RUN dotnet publish -c Release -o out

FROM mcr.microsoft.com/dotnet/runtime:9.0
WORKDIR /sensor-reader

COPY --from=build /sensor-reader/out ./

ENTRYPOINT ["dotnet", "Tms.Backend.SensorReader.dll"]
