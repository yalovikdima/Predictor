-keeppackagenames com.origins.predictor.**
-keep class com.origins.predictor.Predictor{
  public <methods>;
  public static final com.origins.predictor.Predictor INSTANCE;
}

-keep enum com.origins.predictor.Predictor$Api{
        <fields>;
}

-keep class com.origins.predictor.PredictorAnalytics{
        public<methods>;
}

-keep class com.origins.predictor.domain.game.models.ScoreEntity{ *; }
-keep class com.origins.predictor.domain.game.models.GameEntity{ *; }

-keep class * extends com.origins.predictor.base.coreui.fragments.PredictorBaseFragment

-keep class com.origins.predictor.features.routers.base.PredictorNavHostFragment