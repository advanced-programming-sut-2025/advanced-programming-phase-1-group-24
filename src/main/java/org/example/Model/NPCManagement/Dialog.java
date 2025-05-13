package org.example.Model.NPCManagement;

import org.example.Model.Result;
import org.example.Model.TimeManagement.Season;
import org.example.Model.TimeManagement.WeatherType;

import java.util.ArrayList;

public enum Dialog {
    // SUNNY SEBASTIAN
    Sebastian0Sunny("Sebastian", WeatherType.SUNNY, 0,
            "It’s a nice day, I guess. Not really my thing though.",
            "Fair enough, not everyone’s into the sun."),
    Sebastian1Sunny("Sebastian", WeatherType.SUNNY, 1,
            "Sunny days make me feel restless. Like I should be doing something… but what?",
            "Maybe a ride on your bike would clear your head?"),
    Sebastian2Sunny("Sebastian", WeatherType.SUNNY, 2,
            "You know... I’m starting to appreciate days like this, thanks to you.",
            "Glad I could help change your view."),
    Sebastian3Sunny("Sebastian", WeatherType.SUNNY, 3,
            "Spending a sunny day with you feels... kinda perfect.",
            "That means a lot. I feel the same."),

    // STORMY SEBASTIAN
    Sebastian0Stormy("Sebastian", WeatherType.STORM, 0,
            "Great, another storm. Looks like I'm stuck inside again.",
            "Stormy days are good for staying in, at least."),
    Sebastian1Stormy("Sebastian", WeatherType.STORM, 1,
            "I actually don’t mind storms. There’s something intense about them.",
            "Yeah, the energy’s kind of exciting."),
    Sebastian2Stormy("Sebastian", WeatherType.STORM, 2,
            "Storms used to make me feel alone, but now... not so much.",
            "I'm happy I can be here for you."),
    Sebastian3Stormy("Sebastian", WeatherType.STORM, 3,
            "Thunder and lightning don’t bother me when I’m with you.",
            "That’s sweet. We’ve got this together."),

    // RAINY SEBASTIAN
    Sebastian0Rainy("Sebastian", WeatherType.RAIN, 0,
            "Another rainy day... good excuse to dive into a new game.",
            "Totally. Rainy days are the best for that."),
    Sebastian1Rainy("Sebastian", WeatherType.RAIN, 1,
            "I like the sound of rain. Helps me think.",
            "Same here. It’s calming."),
    Sebastian2Rainy("Sebastian", WeatherType.RAIN, 2,
            "Rainy days aren’t so bad when there’s someone to talk to.",
            "I feel that way too."),
    Sebastian3Rainy("Sebastian", WeatherType.RAIN, 3,
            "There’s something really peaceful about rain when you’re here.",
            "Glad we can share this quiet moment."),

    // SNOWY SEBASTIAN
    Sebastian0Snowy("Sebastian", WeatherType.SNOW, 0,
            "Snow’s cool... till you have to walk through it.",
            "Haha, so true."),
    Sebastian1Snowy("Sebastian", WeatherType.SNOW, 1,
            "I kinda like watching the snow fall. It’s hypnotic.",
            "Yeah, it’s beautiful."),
    Sebastian2Snowy("Sebastian", WeatherType.SNOW, 2,
            "Snowy days make me wanna hole up with a good movie. You in?",
            "Definitely, sounds fun."),
    Sebastian3Snowy("Sebastian", WeatherType.SNOW, 3,
            "Snow feels warmer somehow when you’re around.",
            "That’s really sweet of you."),

    // SUNNY LEAH
    Leah0Sunny("Leah", WeatherType.SUNNY, 0,
            "Such a bright day! Perfect for gathering some inspiration outside.",
            "Sounds like a great idea."),
    Leah1Sunny("Leah", WeatherType.SUNNY, 1,
            "I love sketching by the river on sunny days. The light hits just right.",
            "I’d love to see your sketches sometime."),
    Leah2Sunny("Leah", WeatherType.SUNNY, 2,
            "Being out in the sun with you makes everything more beautiful.",
            "That’s so kind of you to say."),
    Leah3Sunny("Leah", WeatherType.SUNNY, 3,
            "Today’s perfect. Sun’s out, and I get to spend it with you.",
            "I wouldn’t want to be anywhere else."),

    // STORMY LEAH
    Leah0Stormy("Leah", WeatherType.STORM, 0,
            "This storm’s wild... I hope my sculptures don’t get knocked over!",
            "Fingers crossed they hold up okay."),
    Leah1Stormy("Leah", WeatherType.STORM, 1,
            "Storms are powerful... kind of inspiring, actually.",
            "Yeah, nature’s force is amazing."),
    Leah2Stormy("Leah", WeatherType.STORM, 2,
            "I’m glad you stopped by. Stormy days feel less lonely with company.",
            "I’m happy to be here for you."),
    Leah3Stormy("Leah", WeatherType.STORM, 3,
            "I love listening to the rain with you by my side. It’s peaceful.",
            "Same here. Let’s just enjoy the moment."),

    // RAINY LEAH
    Leah0Rainy("Leah", WeatherType.RAIN, 0,
            "Rainy days are perfect for working in my cabin.",
            "Hope your projects are going well!"),
    Leah1Rainy("Leah", WeatherType.RAIN, 1,
            "The sound of rain is so relaxing, don’t you think?",
            "Totally, it’s really soothing."),
    Leah2Rainy("Leah", WeatherType.RAIN, 2,
            "I’ve been painting all day. Rain puts me in the creative zone.",
            "That’s awesome. Can’t wait to see your work."),
    Leah3Rainy("Leah", WeatherType.RAIN, 3,
            "I love cozy rainy days with you around.",
            "Same here. It feels special."),

    // SNOWY LEAH
    Leah0Snowy("Leah", WeatherType.SNOW, 0,
            "It’s freezing out! I’m staying bundled up inside today.",
            "Good plan, stay warm!"),
    Leah1Snowy("Leah", WeatherType.SNOW, 1,
            "Snow covers everything like a blank canvas. I love it.",
            "That’s a beautiful way to look at it."),
    Leah2Snowy("Leah", WeatherType.SNOW, 2,
            "I made a snow sculpture earlier. Want to see it later?",
            "I’d love to!"),
    Leah3Snowy("Leah", WeatherType.SNOW, 3,
            "Snowy days are even better when we’re together.",
            "Couldn’t agree more."),

    // SUNNY ABIGAIL
    Abigail0Sunny("Abigail", WeatherType.SUNNY, 0,
            "It’s way too bright out… I’d rather be in the mines.",
            "Haha, classic you."),
    Abigail1Sunny("Abigail", WeatherType.SUNNY, 1,
            "I guess a sunny day’s not *that* bad if you’re around.",
            "Glad I make it bearable!"),
    Abigail2Sunny("Abigail", WeatherType.SUNNY, 2,
            "Maybe we should do something adventurous today!",
            "I’m always up for that."),
    Abigail3Sunny("Abigail", WeatherType.SUNNY, 3,
            "Being outside with you makes the sun seem… nicer.",
            "Aww, that’s sweet of you."),

    // STORMY ABIGAIL
    Abigail0Stormy("Abigail", WeatherType.STORM, 0,
            "Oof, rough weather. Perfect for some gaming indoors.",
            "Count me in!"),
    Abigail1Stormy("Abigail", WeatherType.STORM, 1,
            "Thunder’s awesome, don’t you think? It’s kinda exciting.",
            "Yeah, it really is."),
    Abigail2Stormy("Abigail", WeatherType.STORM, 2,
            "I always feel braver during storms. Weird, huh?",
            "Not weird at all, just cool."),
    Abigail3Stormy("Abigail", WeatherType.STORM, 3,
            "I’m glad you’re here. Makes the storm feel cozy.",
            "Same here, Abigail."),

    // RAINY ABIGAIL
    Abigail0Rainy("Abigail", WeatherType.RAIN, 0,
            "Rainy days make me wanna nap all afternoon.",
            "That sounds kinda nice."),
    Abigail1Rainy("Abigail", WeatherType.RAIN, 1,
            "The rain’s peaceful, but I’m itching for some action.",
            "Maybe the mines later?"),
    Abigail2Rainy("Abigail", WeatherType.RAIN, 2,
            "I love having you around on days like this.",
            "Me too."),
    Abigail3Rainy("Abigail", WeatherType.RAIN, 3,
            "You always know how to brighten a gloomy day.",
            "That means a lot."),

    // SNOWY ABIGAIL
    Abigail0Snowy("Abigail", WeatherType.SNOW, 0,
            "It’s freezing! Let’s duel with snowballs later.",
            "Haha, you’re on!"),
    Abigail1Snowy("Abigail", WeatherType.SNOW, 1,
            "Snow makes everything look magical, don’t you think?",
            "Yeah, it’s beautiful."),
    Abigail2Snowy("Abigail", WeatherType.SNOW, 2,
            "You and I should build a snow fort sometime.",
            "That sounds amazing."),
    Abigail3Snowy("Abigail", WeatherType.SNOW, 3,
            "Snow days with you are my favorite kind of days.",
            "Mine too."),

    // SUNNY ROBIN
    Robin0Sunny("Robin", WeatherType.SUNNY, 0,
            "Perfect day for some outdoor carpentry!",
            "Hope your projects go smoothly!"),
    Robin1Sunny("Robin", WeatherType.SUNNY, 1,
            "I could work all day when the weather’s like this.",
            "You’re always so motivated."),
    Robin2Sunny("Robin", WeatherType.SUNNY, 2,
            "It’s great having someone like you around on sunny days.",
            "Thanks, Robin!"),
    Robin3Sunny("Robin", WeatherType.SUNNY, 3,
            "I love these days even more when we spend them together.",
            "That’s really nice to hear."),

    // STORMY ROBIN
    Robin0Stormy("Robin", WeatherType.STORM, 0,
            "Well, no outdoor work today. Time to plan new projects.",
            "Smart move."),
    Robin1Stormy("Robin", WeatherType.STORM, 1,
            "I actually enjoy storms. They give me creative ideas.",
            "That’s awesome."),
    Robin2Stormy("Robin", WeatherType.STORM, 2,
            "Storms feel less gloomy with good company.",
            "Happy to be here."),
    Robin3Stormy("Robin", WeatherType.STORM, 3,
            "The thunder’s intense, but having you here keeps it calm.",
            "That’s sweet, Robin."),

    // RAINY ROBIN
    Robin0Rainy("Robin", WeatherType.RAIN, 0,
            "Rainy days mean it’s time to tidy up the workshop.",
            "Hope it’s not too messy!"),
    Robin1Rainy("Robin", WeatherType.RAIN, 1,
            "I love listening to the rain while I work.",
            "It’s really relaxing."),
    Robin2Rainy("Robin", WeatherType.RAIN, 2,
            "You make even the rainiest days feel bright.",
            "Aww, thanks Robin."),
    Robin3Rainy("Robin", WeatherType.RAIN, 3,
            "Sharing these quiet, rainy moments with you is the best.",
            "I feel the same way."),

    // SNOWY ROBIN
    Robin0Snowy("Robin", WeatherType.SNOW, 0,
            "Snow’s piling up fast. Better stay inside today.",
            "Yeah, stay warm!"),
    Robin1Snowy("Robin", WeatherType.SNOW, 1,
            "I love how peaceful everything looks under the snow.",
            "It’s so calming."),
    Robin2Snowy("Robin", WeatherType.SNOW, 2,
            "A snowy day’s perfect for hot cocoa and chatting.",
            "Sounds perfect."),
    Robin3Snowy("Robin", WeatherType.SNOW, 3,
            "Days like this are so much better with you here.",
            "That’s lovely of you to say."),

    // SUNNY HARVEY
    Harvey0Sunny("Harvey", WeatherType.SUNNY, 0,
            "Ah, a clear day! Great for getting fresh air.",
            "Definitely, enjoy it!"),
    Harvey1Sunny("Harvey", WeatherType.SUNNY, 1,
            "Sunny days always lift my mood a bit.",
            "Glad to hear that."),
    Harvey2Sunny("Harvey", WeatherType.SUNNY, 2,
            "It’s nice to take a break outside with you.",
            "Same here, Harvey."),
    Harvey3Sunny("Harvey", WeatherType.SUNNY, 3,
            "A sunny day feels even brighter with you around.",
            "That’s so sweet of you."),

    // STORMY HARVEY
    Harvey0Stormy("Harvey", WeatherType.STORM, 0,
            "Be careful out there—the storm’s pretty intense.",
            "Thanks for the heads-up!"),
    Harvey1Stormy("Harvey", WeatherType.STORM, 1,
            "Stormy weather makes me worry about everyone’s safety.",
            "We appreciate you looking out for us."),
    Harvey2Stormy("Harvey", WeatherType.STORM, 2,
            "Having someone like you around makes stormy days easier.",
            "That means a lot, Harvey."),
    Harvey3Stormy("Harvey", WeatherType.STORM, 3,
            "Storms don’t seem so bad when we’re together.",
            "I feel the same way."),

    // RAINY HARVEY
    Harvey0Rainy("Harvey", WeatherType.RAIN, 0,
            "Rain’s good for the crops, but stay dry out there!",
            "Thanks, I will!"),
    Harvey1Rainy("Harvey", WeatherType.RAIN, 1,
            "Rainy days are a good excuse to catch up on paperwork.",
            "Hopefully it’s not too boring."),
    Harvey2Rainy("Harvey", WeatherType.RAIN, 2,
            "I’m glad you stopped by. Rainy days can get lonely.",
            "Happy to keep you company."),
    Harvey3Rainy("Harvey", WeatherType.RAIN, 3,
            "Spending a rainy day with you makes everything better.",
            "That’s really nice of you."),

    // SNOWY HARVEY
    Harvey0Snowy("Harvey", WeatherType.SNOW, 0,
            "Careful on those icy roads out there!",
            "Thanks for the warning."),
    Harvey1Snowy("Harvey", WeatherType.SNOW, 1,
            "Snow always makes the clinic extra quiet.",
            "Hope you get a bit of rest then!"),
    Harvey2Snowy("Harvey", WeatherType.SNOW, 2,
            "It’s nice to share a bit of warmth with you on a cold day.",
            "That’s so kind of you."),
    Harvey3Snowy("Harvey", WeatherType.SNOW, 3,
            "I love quiet snowy days with you by my side.",
            "Me too, Harvey.");


    private final String npcName;
    private final String npcSentence;
    private final String userResponse;
    private final WeatherType weatherType;
    private final int requiredFriendshipLevel;

    Dialog(String npcName,WeatherType weatherType,  int requiredFriendshipLevel,
           String npcSentence, String userResponse) {
        this.npcName = npcName;
        this.npcSentence = npcSentence;
        this.userResponse = userResponse;
        this.weatherType = weatherType;
        this.requiredFriendshipLevel = requiredFriendshipLevel;
    }

    public Result useDialog(){
        StringBuilder dialog = new StringBuilder();
        dialog.append(npcSentence + "\n");
        dialog.append(userResponse + "\n");
        return new Result(true, dialog.toString());
    }

    public String getNpcName() {
        return npcName;
    }

    public String getNpcSentence() {
        return npcSentence;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public int getRequiredFriendshipLevel() {
        return requiredFriendshipLevel;
    }

    public static ArrayList<Dialog> getDialogs(String npcName) {
        ArrayList<Dialog> dialogs = new ArrayList<Dialog>();
        for (Dialog dialog : Dialog.values()) {
            if (dialog.getNpcName().equals(npcName)) {
                dialogs.add(dialog);
            }
        }
        return dialogs;
    }
}