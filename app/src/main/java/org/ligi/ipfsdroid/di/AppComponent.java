package org.ligi.ipfsdroid.di;

import dagger.Component;
import javax.inject.Singleton;
import org.ligi.ipfsdroid.activities.broadcasters.BroadCastersActivity;
import org.ligi.ipfsdroid.activities.MainActivity;
import org.ligi.ipfsdroid.activities.downloads.DownloadsActivity;
import org.ligi.ipfsdroid.activities.feed.FeedActivity;
import org.ligi.ipfsdroid.activities.player.ExoplayerActivity;
import org.ligi.ipfsdroid.activities.player.PlayerActivity;
import org.ligi.ipfsdroid.repository.Repository;
import org.ligi.ipfsdroid.tv.TvActivity;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(BroadCastersActivity broadCastersActivity);

    void inject(PlayerActivity playerActivity);

    void inject(ExoplayerActivity exoplayerActivity);

    void inject(FeedActivity feedActivity);

    void inject(DownloadsActivity downloadsActivity);

    void inject(Repository repository);

    void inject(TvActivity tvActivity);

}
