package fr.strada.smobile.utils;

import fr.strada.smobile.utils.cardlib.models.ActivityChangeInfo;
import fr.strada.smobile.utils.cardlib.models.ActivityDailyRecords;
import fr.strada.smobile.utils.cardlib.models.CardDownload;
import fr.strada.smobile.utils.cardlib.models.CardDriverActivity;
import fr.strada.smobile.utils.cardlib.models.CardIdentification;
import fr.strada.smobile.utils.cardlib.models.CardNumber;
import fr.strada.smobile.utils.cardlib.models.CardVehicleRecords;
import fr.strada.smobile.utils.cardlib.models.CardVehiclesUsed;
import fr.strada.smobile.utils.cardlib.models.Comments;
import fr.strada.smobile.utils.cardlib.models.Document;
import fr.strada.smobile.utils.cardlib.models.LectureHistory;
import fr.strada.smobile.utils.cardlib.models.Notifications;
import fr.strada.smobile.utils.cardlib.models.VehicleRegistration;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmManager {
    private static Realm mRealm;

    public static Realm open() {
        mRealm = Realm.getDefaultInstance();
        return mRealm;
    }

    public static void close() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    public static void saveCardDriverActivity(final CardDriverActivity o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                //mRealm.delete(CardDriverActivity.class);
                realm.copyToRealmOrUpdate(o);
            }
        });
    }

    public static void saveHistory(final LectureHistory o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.copyToRealm(o);
            }
        });
    }

    public static void updateLectureHistory(final String time, final String newFile)
    {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                LectureHistory lectureHistory = realm.where(LectureHistory.class).equalTo("time",time).findFirst();
                lectureHistory.setFile(newFile);
            }
        });
    }

    public static void saveCardDownload(final CardDownload o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(o);
            }
        });
    }

    public static void saveDoc(final Document o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.copyToRealm(o);
            }
        });
    }

    public static void saveComment(final Comments o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.copyToRealm(o);
            }
        });
    }

    public static void saveNotification(final Notifications o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.copyToRealm(o);
            }
        });
    }

    public static void UpdateDoc(final Document o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.insertOrUpdate(o);
            }
        });
    }

    public static void UpdateNotification(final Notifications o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.insertOrUpdate(o);
            }
        });
    }

    public static void UpdateDocField(final String Id , final Boolean o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                Document result = realm.where(Document.class).equalTo("docId",Id).findFirst();
                result.setNotification(o);
            }
        });
    }

    public static void deleteByTimeHistory(final String time) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<LectureHistory> result = realm.where(LectureHistory.class).equalTo("time",time).findAll();
                if (!result.isEmpty())
                result.deleteAllFromRealm();
            }
        });
    }

    public static void deleteDocument(final String docEcheance) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Document result = realm.where(Document.class).equalTo("docId",docEcheance).findFirst();
                if (result != null)
                    result.deleteFromRealm();
            }
        });
    }

    public static void deleteComments(final String commID) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Comments> result = realm.where(Comments.class).equalTo("commID",commID).findAll();
                if (!result.isEmpty())
                result.deleteAllFromRealm();
            }
        });
    }

    public static void UpdateComments(final Comments o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.insertOrUpdate(o);
            }
        });
    }

    public static void deleteNotification(final String id) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm)
            {
                Notifications result = realm.where(Notifications.class).equalTo("id",id).findFirst();
                if (result != null)
                    result.deleteFromRealm();
            }
        });
    }

    public static void UpdateNotification(final String id, final Boolean check) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Notifications result = realm.where(Notifications.class).equalTo("id",id).findFirst();
                if (result != null)
                    result.setCheckEd(check);
            }
        });
    }
    public static void UpdateCardIdentificationNotification(final Boolean check) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CardIdentification result = loadAllCardIdentification();
                if (result != null)
                    result.setNotificationChecked(check);
            }
        });
    }

    public static void UpdateLastDownloadNotification(final Boolean check) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CardDownload result = loadCardDownload();
                if (result != null)
                    result.setNotificationChecked(check);
            }
        });
    }

    public static void saveCardVehiclesUsed(final CardVehiclesUsed o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.copyToRealm(o);
            }
        });
    }

    public static void saveCardIdentification(final CardIdentification o) {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                mRealm.delete(CardIdentification.class);
                realm.copyToRealm(o);
            }
        });
    }

    public static RealmResults<LectureHistory> loadHistory()
    {
        return mRealm.where(LectureHistory.class).findAllSorted("time", Sort.DESCENDING);
    }

    public static RealmResults<LectureHistory> loadHistoryByUser(String emailUser)
    {
        return mRealm.where(LectureHistory.class).equalTo("emailUser",emailUser).findAllSorted("time", Sort.DESCENDING);
    }

    public static LectureHistory loadLastlectureHistoryByUser(String emailUser)
    {
        RealmResults<LectureHistory> allLectureHistorys = mRealm.where(LectureHistory.class)
                                                          .equalTo("emailUser",emailUser)
                                                          .findAllSorted("time");
        if(allLectureHistorys.size()>0){
            return allLectureHistorys.last();
        }else{
            return null;
        }
    }

    public static CardDownload loadCardDownload()
    {
        if (mRealm.where(CardDownload.class).findFirst()!=null)
            return mRealm.where(CardDownload.class).findFirst();
        else return null;
    }

    public static RealmResults<Document> loadDocs()
    {
        return mRealm.where(Document.class).findAll();
    }

    public static RealmResults<Document> loadDocsByUser(String emailUser)
    {
        return mRealm.where(Document.class).equalTo("emailUser",emailUser).findAll();
    }

    public static RealmResults<Notifications> loadNotifications()
    {
        return mRealm.where(Notifications.class).findAll();
    }

    public static RealmResults<Notifications> loadNotificationsByUser(String emailUser)
    {
        return mRealm.where(Notifications.class).equalTo("emailUser",emailUser).findAll();
    }

    public static RealmResults<Document> loadFilteredDocs(String type, Sort sort)
    {
        return mRealm.where(Document.class).findAllSorted(type, sort);
    }

    public static RealmResults<Document> loadFilteredDocsByUser(String type, Sort sort, String emailUser)
    {
        return mRealm.where(Document.class).equalTo("emailUser",emailUser).findAllSorted(type, sort);
    }

    public static Document loadDocs(String id)
    {
        return mRealm.where(Document.class).equalTo("docId",id).findFirst();
    }

    public static RealmResults<Comments> loadComments(String date)
    {
        return mRealm.where(Comments.class).equalTo("date",date).findAll();
    }

    public static RealmResults<Comments> loadCommentsByUser(String date, String emailUser)
    {
        return mRealm.where(Comments.class).equalTo("date",date).equalTo("emailUser", emailUser).findAll();
    }

    public static RealmResults<CardDriverActivity> loadAllCardDriverActivity()
    {
        return mRealm.where(CardDriverActivity.class).findAll();
    }

    public static CardIdentification loadAllCardIdentification()
    {
        return mRealm.where(CardIdentification.class).findFirst();
    }

    public static RealmResults<CardVehiclesUsed> loadAllCardVehiclesUsed()
    {
        return mRealm.where(CardVehiclesUsed.class).findAllAsync();
    }

    public static void clear() {
        checkForOpenRealm();
        mRealm.executeTransaction(new Transaction() {
            public void execute(Realm realm) {
                realm.delete(CardDriverActivity.class);
                realm.delete(CardIdentification.class);
                realm.delete(CardVehiclesUsed.class);
                // realm.delete(Document.class);
                // realm.delete(LectureHistory.class);
                // realm.delete(Comments.class);
                realm.delete(CardDownload.class);
                realm.delete(ActivityDailyRecords.class);
                realm.delete(ActivityChangeInfo.class);
                realm.delete(CardNumber.class);
                realm.delete(CardVehicleRecords.class);
                realm.delete(VehicleRegistration.class);
                //realm.delete(Notifications.class);
            }
        });
    }

    public static void clearCard() {
        checkForOpenRealm();
        mRealm.executeTransaction( new Transaction() {
            public void execute(Realm realm)
            {
                realm.delete(CardDriverActivity.class);
                realm.delete(ActivityDailyRecords.class);
                realm.delete(ActivityChangeInfo.class);
                realm.delete(CardNumber.class);
                realm.delete(CardIdentification.class);
                realm.delete(CardVehiclesUsed.class);
                //realm.delete(LectureHistory.class);
                realm.delete(CardDownload.class);
                realm.delete(CardVehicleRecords.class);
                realm.delete(VehicleRegistration.class);
            }
        });
    }

    public static void clearActivitiesAndVehicule() {
        checkForOpenRealm();
        mRealm.executeTransaction( new Transaction() {
            public void execute(Realm realm)
            {
                realm.delete(CardNumber.class);
                realm.delete(ActivityChangeInfo.class);

                realm.delete(CardVehiclesUsed.class);
                realm.delete(CardVehicleRecords.class);
                realm.delete(VehicleRegistration.class);
            }
        });
    }

    private static void checkForOpenRealm() {
        if (mRealm == null || mRealm.isClosed())
        {
            throw new IllegalStateException("RealmManager: Realm is closed, call open() method first");
        }
    }
}
