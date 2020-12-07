package com.audioplayer.androidaudiostreaming.zoom;

import android.os.Environment;

public interface AppConstant {
    //@formatter:off
    // Analytics AppUI   (1) event_source
    public String APP_UI                           = "APP_UI";
    public String COUNT                            = "COUNT";
    public String ONE                              = "1";
    public String START                            = "START";
    public String SUCCESS                          = "SUCCESS";
    public String DURATION                         = "DURATION";
    public String VALUE                            = "VALUE";
    public String CONSTANT_START_EVENT             = "_START_EVENT";
    public String CONSTANT_PLAYED_EVENT            = "_PLAYED_EVENT";
    public String CONSTANT_EXPRESSION_NAME_EVENT   = "_EXPRESSION_NAME_EVENT";
    public String CONSTANT_END_EVENT               = "_END_EVENT";
    public String CONSTANT_SCORE                   = "_SCORE";
    public String CONSTANT_PLAYED_END_EVENT        = "_PLAYED_END_EVENT";
    public String CONSTANT_LEVEL_PLAYED_EVENT      = "_LEVEL_START_EVENT";
    public String CONSTANT_LEVEL_RESULT_EVENT      = "_LEVEL_RESULT_EVENT";

    public String CONSTANT_MATCH_DETAIL            = "_MATCH_DETAILS_VIEWED";
    String BA_CATEGORY_SELECTED                   = "_CATEGORY_SELECTED";

    public String GAME_PLAYED_EVENT                = "_PLAYED_THEME_EVENT";
    public String GAME_END_EVENT                   = "_END_THEME_EVENT";
    public String GAME_RESULT_EVENT                = "_RESULT";
    public String GAME_THEME_EVENT                 = "_THEME";
    public String GAME_IMAGE_EVENT                 = "_IMAGE";
    public String GAME_GRID_SIZE_EVENT             = "_GRID_SIZE";

    public String LEVEL_1                          = "BEGINNER";
    public String LEVEL_2                          = "INTERMEDIATE";
    public String LEVEL_3                          = "ADVANCED";
    public String GAME_RESULT_MOVES_EVENT          = "_RESULT_MOVES";
    public String GAME_RESULT_TIME_EVENT           = "_RESULT_TIME";
    public String GAME_CARD_EVENT                  = "_CARD";
    public String WORD_OF_THE_DAY_COUNT            = "_COUNT";
    public String INSPIRATIONAL_QUOTE_COUNT        = "_COUNT";
    public String GAME_RESULT_NO_OF_RIGHT_ANSWERS_EVENT       = "_NO_OF_RIGHT_ANSWERS";
    public String GAME_RESULT_NO_OF_TOTAL_QUESTIONS_EVENT     = "_NO_OF_TOTAL_QUESTIONS";

    // Story Start Event
    public String CONSTANT_STORY                   = "STORY_";



    //QUIZ APP

    // SCORES Quiz

    String QUESTION_ANSWERED                       = "QUESTION_ANSWERED";
    String CORRECT_ANS                             = "CORRECT_ANS";
    String WRONG_ANS                               = "WRONG_ANS";


    int INSTALL_APP                                = 0;
    int INSTALLED_APP                              = 1;
    int UPDATE_APP                                 = 2;
    int UNINSTALL_APP                              = 3;
    int ACTIVATE_APP                               = 4;
    int DEACTIVATE_APP                             = 5;
    int KLUG_UPDATE_APP                            = 6;
    int SDCARD_APP_ACTIVE                          = 1;
    int SDCARD_APP_DEACTIVE                        = 2;
    int BUNDLE_EXPIRED                             = 5;
    int ACTIVE_BUNDLE                              = 1;

    String HIDDEN_KLUG_LOCATION                    = "/.klug";
    String KLUG_LOCATION                           = "/klug";
    String APPS_EXTRACT_LOCATION                   = "/klug/APPS/";
    String STORY_APPS_EXTRACT_LOCATION             = "/StoryApps/";
    String FUN_APPS_EXTRACT_LOCATION               = "/FunApps/";
    String QUIZ_APPS_EXTRACT_LOCATION              = "/QuizApps/";
    String VIDEO_STREAMING_APPS_EXTRACT_LOCATION   = "/VideoStreamingApps/";
    String TIKTOKTOE_APPS_EXTRACT_LOCATION         = "/TikTokToe/";
    String MEMORY_GAME_APPS_EXTRACT_LOCATION       = "/MemoryGame/";
    String MAIN_APPS_EXTRACT_LOCATION              = "/";
    String INSPIRATIONAL_APPS_EXTRACT_LOCATION     = "/InspirationalQuotes/";
    String OPTION_GAME_APPS_EXTRACT_LOCATION       = "/OptionGame/";
    String MATH_OPERATOR_APPS_EXTRACT_LOCATION     = "/MathOperator/";
    String BUNDLE_APPS_EXTRACT_LOCATION            = "/Bundle/";
    String FONT_LOCATION                           = "/klug/APPS/fonts/";

    String APPS_STOP_EXP_LOCATION                  = Environment.getExternalStorageDirectory() + "/klug/APPS/stop_motion.txt";

    String LOG_E                                   = "e";
    String LOG_I                                   = "i";
    String LOG_D                                   = "d";

    int PLAY_GAME_EXPRESSION_WITH_CALLBACK         = 133;
    int EXPRESSION_DATA                            = 135;
    int EXPRESSION_IMAGE_PATH                      = 136;
    int EXPRESSION_AUDIO_PATH                      = 137;

    String APP_TYPE_STORY                          = "StoryApp";
    String APP_TYPE_FUN                            = "FunApp";
    String APP_TYPE_VIDEO_STREAMING                = "VideoStreamingApp";
    String APP_TYPE_QUIZ                           = "QuizApp";
    String APP_TYPE_GALLERY                        = "Photos";
    String TIKTOKTOE                               = "TikTokToe";
    String MEMORY_GAME                             = "MemoryGame";
    String ROBOT_REACTOR                           = "RobotReactor";
    String PUZZLE_GAME                             = "PuzzleGame";
    String CALCULATOR                              = "Calculator";
    String OPTION_GAME                             = "OptionGame";
    String MATH_OPERATOR                           = "MathOperator";
    String SNAKE_GAME                              = "SnakeGame";
    String CODE_MASTER                             = "CodeMaster";
    String APP_TYPE_CLASS_ROOM_HINDI               = "ClassRoomHindi";
    String APP_TYPE_TTM                            = "TTM";
    String APP_TYPE_BUNDLE                         = "Bundle";
    String HINDI                                   = "HINDI";
    String ENGLISH                                 = "ENGLISH";
    String CRICKET_INFO                            = "CricketInfo";
    String CRICKET_BUZZ                            = "CricketBuzz";

    String MIKO_COACH                               = "MikoCoach";
    String KLUG_UPDATE                             = "KlugUpdate";

    String INSPIRATIONAL_QUOTES                    = "InspirationalQuotes";
    String WORD_OF_THE_DAY                         = "Wordoftheday";
    String TABLES                                  = "Tables";
    String CLOCK                                   = "Clock";
    String Flag_Trivia                             = "Flag Trivia";
    String Navneet                                 = "Navneet";
    String QUIZ_EASY                               = "_EASY";
    String QUIZ_MEDIUM                             = "_MEDIUM";
    String QUIZ_HARD                               = "_HARD";

    String JOB_PICKEDUP                            = "PICKEDUP";
    String JOB_SCHEDULED                           = "SCHEDULED";
    String JOB_INSTALL                             = "INSTALL";
    String JOB_UNINSTALL                           = "UNINSTALL";
    String JOB_UPDATE                              = "UPDATE";
    String JOB_COMPLETED                           = "COMPLETED";
    String JOB_ERROR                               = "ERROR";
    String JOB_DEACTIVATE                          = "DEACTIVATE";
    String JOB_UPDATE_EXPIRY                       = "UPDATE_EXPIRY";

    int RESPONSE_CODE_200                          = 200;
    int RESPONSE_CODE_404                          = 404;

    String APPSTORE_BASE_URL                       = "APPSTORE_BASE_URL";
    String REGION                                  = "appStoreRegion";//REGION
    String ENABLE_APPSTORE_DELETE                  = "ENABLE_APPSTORE_DELETE";
    String ENABLE_SDCARD_INSTALLATION              = "ENABLE_SDCARD_INSTALLATION";
    String API_UPDATE_SUBSCRIPTION                 = "miko/updateSubscription";
    String API_UPDATE_JOB                          = "miko/updatejob";
    String API_GET_JOB                             = "miko/getjobs";
    String VIDEO_STREAMING_STORY_API               = "getVideostreamingAppsData";
    String SDCARD0_AVAILABLE_SPACE                 = "Sdcard0_Available_Space";
    String SDCARD1_AVAILABLE_SPACE                 = "Sdcard1_Available_Space";
    String KEY                                     = "key";
    String VALUE_L                                 = "value";
    String SENDBACKENDINSTALLEDAPPS                = "miko/app-verification";

    /* Cricket Info */
    String API_CRICKET_INFO                        = "CRICLIVE_URL";
    String API_POST_QUICK_UPDATE                   = "/quick_update";
    String API_POST_MATCH_TYPE                     = "/get_match_type_details";
    String API_POST_MATCH_DETAIL                   = "/get_match_details";
    String API_POST_ALL_TEAM                       = "/get_all_teams";
    String API_POST_PLAYER_NAME                    = "/get_player_names";
    String API_POST_PLAYER_DETAIL                  = "/get_player_details";
    String API_POST_FOLLOW_TEAM                    = "/follow_team";
    String API_POST_UN_FOLLOW_TEAM                 = "/unfollow_team";
    String API_POST_FOLLOW_PLAYER                  = "/follow_player";
    String API_POST_UN_FOLLOW_PLAYER               = "/unfollow_player";
    String API_POST_POINTS_TABLE                   = "/points_table";

    public String CONSTANT_SEPARATOR               = "-";

    //@formatter:on

    // MIX Panel
    String MIXPANELENABLE                            = "MIXPANELENABLE";
    String MIXPANELTOKEN                             = "MIXPANELTOKEN";
    String YESEnable                                 = "YES";
    String USER_TIMEZONE                             = "USER_TIMEZONE";
    String VOICE_NAME                                = "VOICE_NAME";
    String BOT_REGION                                = "REGION";

    //Mix Panel Games
    String ViewContent                              = "View Content";
    String Start                                    = "Start";
    String End                                      = "End";
    String Content                                  = "Content";
    String Story                                    = "Story";
    String Games                                    = "Games";
    String Abort                                    = "Abort";

    /* Cricko Live */
    String Matches                                  = "Matches";
    String Details                                  = "Details";
    String ScoreBoard                               = "Scorecard";
    String PlayerInfo                               = "Player Info";
    String PointsTable                              = "Points Table";

    /* Mixpanel WOTD */
    String Word                                     = "Word";
    String Meaning                                  = "Meaning";
    String Example                                  = "Example";

    /* Mixpanel VideoStreaming */
    String Internet                                 = "No Internet";

    /* Mixpanel Mikode */
    String ConnectionLost                           = "Connection Lost";
    String mikodeLowBattery                         = "Battery Low";
    String mikodeOnCharging                         = "On Charge";
    String mikodeObstacle                           = "Obstacle";
    String mikodeRun                                = "Code Run";
    String star1                                    = "1 Star";
    String star2                                    = "2 Star";
    String star3                                    = "3 Star";
    String extraCredit                              = "Extra Credit";
    String starActivity                             = "Activity Star";

    /* Mixpanel Robot Reactor */
    String Reward                                 = "Reward";

    /* MixPanel Memory */
    String Theme                                    = "_Theme";
    String Card                                    = "_Card";

    /* MixPanel Quiz */
    String Won                                     = "Won";
    String Lost                                    = "Lost";
    String Draw                                    = "Draw";
    String MaxQuestions                            = "15";

    //Mix Panel Mikoji
    String Opened                                   = " Opened"; // Keep the space before Opened

    //Mix Panel Settings
    String Settings                                             = "Settings";
    String Selected                                             = " Selected"; // Keep the space before Selected
    String _UP                                                  = "_UP";
    String _UP_FT                                               = "_UP_FT";
    String _UP_VT                                               = "_UP_VT";
    String Restart                                              = "Restart";
    String UserProfile                                          = "User Profile";
    String FaceTraining                                         = "Face Training";
    String VoiceTraining                                        = "Voice Training";
    String DND                                                  = "DND";
    String Activated                                            = "Activated";
    String Deactivated                                          = "Deactivated";
    String Success                                              = "Success";
    String Failure                                              = "Failure";

    //Mix Panel Camera
    String Camera                                   = "Camera";
    String PhotosClicked                            = "Photos Clicked";
    String clickStarted                             = "Click Started";
    String photoStatus                              = "Photo Status";
    String _Clicked                                 = "_Clicked";
    String Auto                                     = "Auto";
    String Click                                    = "Click";
    String keepPhoto                                = "Keep";
    String discardPhoto                             = "Discard";
    String UnderScore                               = "_";

    //MP Gallery
    String Photos                                               = "Photos";
    String PhotosViewed                                         = "Photos Viewed";
    String Countofthenumberofphotos                             = "Count of the number of photos";
    String Photos_View                                          = "Photos_View";
    String Deleted                                              = "Deleted";

    //MP KW
    String KnowledgeWorld                                       = "Knowledge World";
    String Graph                                                = "Graph";
    String KW                                                   = "KW";
    String FUN                                                  = "FUN";
    String APP_STORE_REGION                                     = "APP_STORE_REGION";
    String Miko2                                                = "Miko2";

    //MP KW-Hindi
    String AB_HINDI_HO_JAAYE                                    = "Ab Hindi Ho Jaaye";
    String MODULE_INSTLLED                                      = "Installed";
    String MODULE_UNINSTLLED                                    = "Uninstalled";
    String MAIN_MENU                                            = "Main Menu";
    String NA                                                   = "NA";
    String APP_HINDI                                            = "Hindi";
    String MIXPANEL_BUNDLE                                      = "bundle_";

    int TALENT_HOUSE_ALL_GAMES                  = 0;
    int TALENT_HOUSE_BUNDLE_GAMES               = 1;
    int TALENT_HOUSE_FAVOURITE_GAMES            = 2;

    // Unused memory in bot
    String Internal_Memory                                                = "Internal_Memory";
    String External_Memory                                                = "External_Memory";

    String REFRESH                              = "Refresh";

    String HINDI_STARTED                        = "HINDI_STARTED";
    String HINDI_CLOSED                         = "HINDI_CLOSED";
    String ENGLISH_STARTED                      = "ENGLISH_STARTED";
    String ENGLISH_CLOSED                       = "ENGLISH_CLOSED";
    String LAUNCH_DIRECTIVE_STARTED             = ">>>STARTED";
    String LAUNCH_DIRECTIVE_CLOSED              = ">>>CLOSED";
    String LAUNCH_TTM                           = ">>>TTM";

    String CRICKO_BUZZ_STARTED                  = "CRICKO_BUZZ_STARTED";
    String CRICKO_BUZZ_CLOSED                   = "CRICKO_BUZZ_CLOSED";

    String MixPanel_TALENT_HOUSE_ALL_GAMES           = "All Talents";
    String MixPanel_TALENT_HOUSE_FAVOURITE_GAMES     = "Favourites";
    String MixPanel_MIKODE_OPENED_FROM_APP           = "Mikode App";
    String TALENT_HOUSE_VOICE                        = "Voice";
    String MixPanel_TALENT_HOUSE_BUNDLE_GAMES        = "Bundles";
    String TALENT_HOUSE_POPUP                        = "Popup";

    String CAMERA_RESERVE_SIZE                   = "CAMERA_RESERVE_SIZE";
    String BOT_RESERVE_SIZE                      = "BOT_RESERVE_SIZE";


    String APP_KNOWLEDGE_WORLD                   = "KNOWLEDGE_WORLD";

    int GALLERY_OPENED_FROM_HOME                 = 0;
    int GALLERY_OPENED_FROM_CAMERA               = 1;

    // Coach
    int MIKO_COACH_LIST_THREASHOLD_TIME             =  166665;
    String Today                                    =  "Today";
    String Tomorrow                                 =  "Tomorrow";
    String mins                                     =  "mins";
    String hrs                                      =  "hrs";
    String Exit                                     =  "Exit";
    String Join                                     =  "Join";
    String Scroll                                   =  "Scroll";
    String BeforeClass                              =  "The class will commence in ";
    String OnGoingCLass                             =  "The class is in session.";
    String COACH_LOCATION                           = "/APPS/IN/MikoCoach/";
    String minutes                                  = " minutes";
    String MEETING_ID = "89181802802";
    String DISPLAY_NAME = "ZoomUS SDK";
    String USER_ID = "tushar@klugtek.co.in";
    String ZOOM_ACCESS_TOKEN = "pzAQlBmvQricGyV2ycrJuw";

    String WEB_DOMAIN = "zoom.us";
}
