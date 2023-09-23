import com.soheibbettahar.litarus.util.analytics.AnalyticsHelper
import com.soheibbettahar.litarus.util.analytics.FirebaseAnalyticsHelper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AnalyticsModule {

    @Binds
    abstract fun bindAnalyticsHelper(helper: FirebaseAnalyticsHelper): AnalyticsHelper

}