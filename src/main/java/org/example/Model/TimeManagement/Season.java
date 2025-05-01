package org.example.Model.TimeManagement;

import org.example.Model.Growables.SourceType;

import java.util.List;

public enum Season {
    SPRING(
            List.of(WeatherType.SUNNY, WeatherType.RAIN, WeatherType.STORM),
            List.of(
//                    SourceType.CauliflowerSeeds,
//                    SourceType.ParsnipSeeds,
//                    SourceType.PotatoSeeds,
//                    SourceType.JazzSeeds,
//                    SourceType.TulipBulb
            )
    ),
    SUMMER(
            List.of(WeatherType.SUNNY, WeatherType.RAIN, WeatherType.STORM),
            List.of(
//                    SourceType.CornSeeds,
//                    SourceType.PepperSeeds,
//                    SourceType.RadishSeeds,
//                    SourceType.WheatSeeds,
//                    SourceType.PoppySeeds,
//                    SourceType.SunflowerSeeds,
//                    SourceType.SummerSquashSeeds
            )
    ),
    AUTUMN(
            List.of(WeatherType.SUNNY, WeatherType.RAIN, WeatherType.STORM),
            List.of(
//                    SourceType.ArtichokeSeeds,
//                    SourceType.CornSeeds,
//                    SourceType.EggplantSeeds,
//                    SourceType.PumpkinSeeds,
//                    SourceType.SunflowerSeeds,
//                    SourceType.FairySeeds
            )
    ),
    WINTER(
            List.of(WeatherType.SUNNY, WeatherType.SNOW),
            List.of(
                    //  SourceType.PowdermelonSeeds
            )
    );

    private final List<WeatherType> weatherTypes;
    private final List<SourceType> mixedSeeds;

    Season(List<WeatherType> weatherTypes, List<SourceType> mixedSeeds) {
        this.weatherTypes = weatherTypes;
        this.mixedSeeds = mixedSeeds;
    }

    public List<WeatherType> getWeatherTypes() {
        return weatherTypes;
    }

    public List<SourceType> getMixedSeeds() {
        return mixedSeeds;
    }

    public Season next() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}

