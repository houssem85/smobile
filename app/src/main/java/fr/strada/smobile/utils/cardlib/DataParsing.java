package fr.strada.smobile.utils.cardlib;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.strada.smobile.utils.TimeSpan;
import fr.strada.smobile.utils.cardlib.models.ActivityChangeInfo;
import fr.strada.smobile.utils.cardlib.models.ActivityDailyRecords;
import fr.strada.smobile.utils.cardlib.models.ApplicationIdentification;
import fr.strada.smobile.utils.cardlib.models.CardChipIdentification;
import fr.strada.smobile.utils.cardlib.models.CardControlActivityDataRecord;
import fr.strada.smobile.utils.cardlib.models.CardCurrentUse;
import fr.strada.smobile.utils.cardlib.models.CardDownload;
import fr.strada.smobile.utils.cardlib.models.CardDriverActivity;
import fr.strada.smobile.utils.cardlib.models.CardDrivingLicenceInformation;
import fr.strada.smobile.utils.cardlib.models.CardEventData;
import fr.strada.smobile.utils.cardlib.models.CardExtendedSerialNumber;
import fr.strada.smobile.utils.cardlib.models.CardHolderName;
import fr.strada.smobile.utils.cardlib.models.CardIccIdentification;
import fr.strada.smobile.utils.cardlib.models.CardIdentification;
import fr.strada.smobile.utils.cardlib.models.CardNumber;
import fr.strada.smobile.utils.cardlib.models.CardStructureVersion;
import fr.strada.smobile.utils.cardlib.models.CardVehicleRecords;
import fr.strada.smobile.utils.cardlib.models.CardVehiclesUsed;
import fr.strada.smobile.utils.cardlib.models.ControlActivityData;
import fr.strada.smobile.utils.cardlib.models.ControlCardNumber;
import fr.strada.smobile.utils.cardlib.models.ControlVehicleRegistration;
import fr.strada.smobile.utils.cardlib.models.CurrentUsage;
import fr.strada.smobile.utils.cardlib.models.DriverActivityData;
import fr.strada.smobile.utils.cardlib.models.DriverCardApplicationIdentification;
import fr.strada.smobile.utils.cardlib.models.DriverCardHolderIdentification;
import fr.strada.smobile.utils.cardlib.models.DrivingLicenceInfo;
import fr.strada.smobile.utils.cardlib.models.EmbedderIcAssemblerId;
import fr.strada.smobile.utils.cardlib.models.EventVehicleRegistration;
import fr.strada.smobile.utils.cardlib.models.EventsData;
import fr.strada.smobile.utils.cardlib.models.Ic;
import fr.strada.smobile.utils.cardlib.models.Identification;
import fr.strada.smobile.utils.cardlib.models.InfoCard;
import fr.strada.smobile.utils.cardlib.models.SessionOpenVehicle;
import fr.strada.smobile.utils.cardlib.models.VehicleRegistration;
import fr.strada.smobile.utils.cardlib.models.cardEventRecordsInfo;
import io.realm.RealmList;

import static fr.strada.smobile.utils.cardlib.ByteConverter.ToUInt32;

public class DataParsing {

    public static Ic ReadIc(BinaryReader reader)
    {
        Ic ic = new Ic(ReadCardChipIdentification(reader));
        return ic;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static Identification ReadIdentification(BinaryReader reader){
        Identification identification = null;
        try {
            identification = new Identification(ReadCardIdentification(reader),ReadDriverCardHolderIdentification(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return identification;
    }

    public static DriverCardHolderIdentification ReadDriverCardHolderIdentification(BinaryReader reader) throws IOException {
        DriverCardHolderIdentification cardHolderIdentification = new DriverCardHolderIdentification(ReadCardHolderName(reader),reader.ReadTimeReal().toString(),reader.ReadTimeReal().toString());

        //cardHolderIdentification.CardHolderBirthDate = reader.ReadDate();
        //cardHolderIdentification.CardHolderPreferredLanguage = reader.ReadBytes(2);

        return cardHolderIdentification;
    }

    public static CardHolderName ReadCardHolderName(BinaryReader reader) throws IOException {
        CardHolderName cardHolderName = new CardHolderName(ReadIA5String(reader,36),ReadIA5String(reader,36));
        return cardHolderName;
    }

    public static CardIdentification ReadCardIdentification(BinaryReader reader) throws IOException {
        CardIdentification cardIdentification = new CardIdentification();

        cardIdentification.setCardIssuingMemberState(reader.readByte());
        cardIdentification.setCardNumber(ReadCardNumber(reader));

        cardIdentification.setCardIssuingAuthorityName(ReadIA5String(reader,36));

        cardIdentification.setCardIssueDate(reader.ReadTimeReal().toString());
        cardIdentification.setCardValidityBegin(reader.ReadTimeReal().toString());
        cardIdentification.setCardExpiryDate(reader.ReadTimeReal().toString());

        return cardIdentification;
    }

    public static byte[] hexStringToByteArrayy(String s)
    {
        if (s == null)
            return null;

        int stringLength = s.length();//20
        if ((stringLength & 0x1) != 0)
        {
            throw new IllegalArgumentException(
                    "convertHexStringToByteArray requires an even number of hex characters");
        }

        byte[] b = new byte[stringLength / 2];//10

        for (int i = 0, j = 0; i < stringLength; i += 2, j++)
        {
            int high = charToNibble(s.charAt(i));//
            int low = charToNibble(s.charAt(i + 1));
            b[j] = (byte) ((high << 4) | low);
        }
        return b;
    }

    private static int charToNibble(char c)
    {
        if ('0' <= c && c <= '9')
        {
            return c - '0';
        }
        else if ('a' <= c && c <= 'f')
        {
            return c - 'a' + 0xa;
        }
        else if ('A' <= c && c <= 'F')
        {
            return c - 'A' + 0xa;
        }
        else
        {
            throw new IllegalArgumentException("Invalid hex character: " + c);
        }
    }

    public static DriverActivityData ReadDriverActivityData(BinaryReader reader, int activityStructureLength)
    {
        DriverActivityData driverActivityData = new DriverActivityData(ReadCardDriverActivity(reader, activityStructureLength));
        return driverActivityData;
    }

    public static CardDriverActivity ReadCardDriverActivity(BinaryReader reader, int activityStructureLength)
    {
        CardDriverActivity cardDriverActivity = new CardDriverActivity();

        cardDriverActivity.setActivityPointerOldestDayRecord(reader.readWordBE());
        cardDriverActivity.setActivityPointerNewestRecord(reader.readWordBE());
        byte[] dailyRecordsBytes = reader.readBytes(activityStructureLength);

        cardDriverActivity.setActivityDailyRecords(GetCardActivityDailyRecords(dailyRecordsBytes, cardDriverActivity.getActivityPointerOldestDayRecord(), cardDriverActivity.getActivityPointerNewestRecord()));

        return cardDriverActivity;
    }

    private static RealmList<ActivityDailyRecords> GetCardActivityDailyRecords(byte[] dailyRecordsBytes, int activityPointerOldestDayRecord, int activityPointerNewestRecord)
    {
        byte[] fullRecordsBytes = GetOnlyFullRecordsBytes(dailyRecordsBytes, activityPointerOldestDayRecord, activityPointerNewestRecord);

        RealmList<ActivityDailyRecords> records = new RealmList<>();

        if (dailyRecordsBytes.length > 0)
        {
            BinaryReader   r = new BinaryReader(fullRecordsBytes);

            while (r.getPosition() < r.getBytes().length)
            {
                ActivityDailyRecords cardActivityDailyRecord = ReadCardActivityDailyRecord(r);
                records.add(cardActivityDailyRecord);
            }

        }

        RealmList<ActivityDailyRecords> dailyRecords = records;
        return dailyRecords;
    }

    private static ActivityDailyRecords ReadCardActivityDailyRecord(BinaryReader reader)
    {
        ActivityDailyRecords dailyRecord = new ActivityDailyRecords();
        dailyRecord.setActivityPreviousRecordLength(reader.readWordBE());
        dailyRecord.setActivityRecordLength(reader.readWordBE());
        dailyRecord.setActivityRecordDate(reader.ReadTimeReal().toString());
        dailyRecord.setActivityDailyPresenceCounter(reader.readWordBE());
        dailyRecord.setActivityDayDistance(reader.readWordBE());

        int activityInfoLength = (int)(dailyRecord.getActivityRecordLength() - 12);
        byte[] changeInfoBytes = reader.readBytes(activityInfoLength);
        dailyRecord.setActivityChangeInfo(GetActivityChangeInfos(changeInfoBytes));
        return dailyRecord;
    }


    private static RealmList<ActivityChangeInfo> GetActivityChangeInfos(byte[] changeInfoBytes)
    {
        RealmList<ActivityChangeInfo> activityChangeInfos = new RealmList<>();

        if (changeInfoBytes.length > 0)
        {
            BinaryReader  r = new BinaryReader(changeInfoBytes);



            while (r.getPosition() < r.getBytes().length)
            {

                boolean[] bits = byteArray2BitArray(r.readBytes(2));
                // BitArray bits = new BitArray(word.ToBytes());



                ActivityChangeInfo activityChangeInfo = new ActivityChangeInfo();
                activityChangeInfo.setLecteur(InfoCard.EnumMapper.ToLecteur(bits[0]).name());
                activityChangeInfo.setEtatConduite(InfoCard.EnumMapper.ToEtatConduite(bits[1]).name());
                activityChangeInfo.setEtatCarte(InfoCard.EnumMapper.ToEtatCarteConducteur(bits[2]).name());
                boolean[] Activiteskip = Skip(bits,3);
                boolean[] ActiviteTake = Take(Activiteskip,2);

                try {
                    activityChangeInfo.setActivite(InfoCard.EnumMapper.ToActivite(ActiviteTake).name());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean[] TimeSkip = Skip(bits,5);
                boolean[] TimeTake = Take(TimeSkip,11);



                activityChangeInfo.setTime(ToTimeSpan(TimeTake).toString());
                activityChangeInfos.add(activityChangeInfo);
            }

        }

        RealmList<ActivityChangeInfo> changeInfos = activityChangeInfos;
        return changeInfos;
    }

    static int booleansToInt(boolean[] arr){
        int n = 0;
        for (boolean b : arr)
            n = (n << 1) | (b ? 1 : 0);
        return n;
    }

    public static TimeSpan ToTimeSpan(boolean[]  bitArray)
    {
        int nbreMinutes = booleansToInt(bitArray);
        TimeSpan timeSpan = new TimeSpan(TimeSpan.MINUTES,nbreMinutes);//Timer.FromMinutes(nbreMinutes);
        return timeSpan;
    }


    private static byte[] GetOnlyFullRecordsBytes(byte[] dailyRecordsBytes, int activityPointerOldestDayRecord, int activityPointerNewestRecord)
    {
        int activityStructureLength = dailyRecordsBytes.length;

        if (activityStructureLength == 0)
            return new byte[0];



        byte [] shiftRecordsBytes = Shift(dailyRecordsBytes,activityPointerOldestDayRecord);

        // byte[] shiftRecordsBytes = dailyRecordsBytes.Shift(activityPointerOldestDayRecord);

        int activityPointerNewestRecordAfterShift = (activityPointerOldestDayRecord > activityPointerNewestRecord) ?
                (int)(activityStructureLength - activityPointerOldestDayRecord + activityPointerNewestRecord)
                : (int)(activityPointerNewestRecord - activityPointerOldestDayRecord);

        int newestRecordLastByteIndex = GetNewestRecordLastByteIndex(shiftRecordsBytes, activityPointerNewestRecordAfterShift);


        //  byte[] onlyFullRecordsBytes = shiftRecordsBytes.Take(newestRecordLastByteIndex).ToArray();
        byte[] onlyFullRecordsBytes = Take(shiftRecordsBytes,newestRecordLastByteIndex);

        return onlyFullRecordsBytes;
    }

    public static byte[] Shift(byte[] shiftRecordsBytes,int shift){

        byte[] reordered = new byte[shiftRecordsBytes.length];

        for(int i=0; i<shiftRecordsBytes.length;i++)
            reordered[i] = shiftRecordsBytes[(shift+i)%shiftRecordsBytes.length];

        return reordered;
    }


    public static byte[] Skip(byte[] shiftRecordsBytes,int take){
        byte[] reordered ;
        reordered = Arrays.copyOfRange(shiftRecordsBytes, take, shiftRecordsBytes.length);
        return reordered;

    }

    public static boolean[] Skip(boolean[] shiftRecordsBytes,int skip){
        boolean[] reordered ;
        reordered = Arrays.copyOfRange(shiftRecordsBytes, skip, shiftRecordsBytes.length);
        return reordered;

    }

    public static byte[] Take(byte[] shiftRecordsBytes,int take){
        byte[] reordered ;
        reordered = Arrays.copyOfRange(shiftRecordsBytes, 0, take);
        return reordered;
    }

    public static boolean[] Take(boolean[] shiftRecordsBytes,int take){
        boolean[] reordered ;
        reordered = Arrays.copyOfRange(shiftRecordsBytes, 0, take);
        return reordered;
    }

    private static int GetNewestRecordLastByteIndex(byte[] bytes, int activityPointerNewestRecord)
    {


        byte[] vBytes = Skip(bytes,activityPointerNewestRecord);

        BinaryReader r = new BinaryReader(vBytes);

        ActivityDailyRecords cardActivityDailyRecord = ReadCardActivityDailyRecord(r);
        int newestRecordLastByteIndex = (int) (activityPointerNewestRecord + cardActivityDailyRecord.getActivityRecordLength());
        return newestRecordLastByteIndex;

    }

    public static ControlActivityData ReadControlActivityData(BinaryReader reader)
    {
        ControlActivityData controlActivityData = null;
        try {
            controlActivityData = new ControlActivityData(ReadCardControlActivityDataRecord(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return controlActivityData;
    }

    public static CardControlActivityDataRecord ReadCardControlActivityDataRecord(BinaryReader reader) throws IOException {
        CardControlActivityDataRecord cardControlActivityDataRecord =
                new CardControlActivityDataRecord(reader.readByte(),reader.ReadTimeReal().toString(),ReadControlCardNumber(reader),ReadVehicleRegistration(reader),reader.ReadTimeReal().toString(),reader.ReadTimeReal().toString());
        //  cardControlActivityDataRecord.ControlType = reader.ReadByte();
        //  cardControlActivityDataRecord.ControlTime = reader.ReadTimeReal();
        //  cardControlActivityDataRecord.ControlCardNumber = ReadControlCardNumber(reader);
        //  cardControlActivityDataRecord.ControlVehicleRegistration = ReadVehicleRegistrationIdentification(reader);
        //  cardControlActivityDataRecord.ControlDownloadPeriodBegin = reader.ReadTimeReal();
        //  cardControlActivityDataRecord.ControlDownloadPeriodEnd = reader.ReadTimeReal();
        return cardControlActivityDataRecord;
    }

    public static ControlCardNumber ReadControlCardNumber(BinaryReader reader) throws IOException {
        ControlCardNumber controlCardNumber = new ControlCardNumber(reader.readByte(),reader.readByte(),ReadCardNumber(reader));

        return controlCardNumber;
    }

    public static CurrentUsage ReadCurrentUsage(BinaryReader reader)
    {
        CurrentUsage currentUsage = new CurrentUsage(ReadCardCurrentUse(reader));
        return currentUsage;
    }

    public static CardCurrentUse ReadCardCurrentUse(BinaryReader reader)
    {
        CardCurrentUse cardCurrentUse = new CardCurrentUse(reader.ReadTimeReal().toString(),ReadSessionOpenVehicle(reader));
        return cardCurrentUse;
    }

    public static EventsData ReadEventsData(BinaryReader reader, byte noOfEventsPerType)
    {
        EventsData eventsData = new EventsData(ReadCardEventData(reader, noOfEventsPerType));
        return eventsData;
    }

    public static CardEventData ReadCardEventData(BinaryReader reader, byte noOfEventsPerType)
    {
        CardEventData cardEventData = new CardEventData();

        byte typeCount = 6;

        List<cardEventRecordsInfo> cardEventRecords = new ArrayList<>();

        for (byte typeIndex = 0; typeIndex < typeCount; typeIndex++)
        {
            for (byte i = 0; i < noOfEventsPerType; i++)
            {
                cardEventRecordsInfo cardEventRecord = ReadCardEventRecord(reader);
                cardEventRecords.add(cardEventRecord);
            }
        }

        cardEventData.setCardEventRecords(cardEventRecords);

        return cardEventData;
    }

    private  static cardEventRecordsInfo ReadCardEventRecord(BinaryReader reader)
    {
        byte eventType = reader.readByte();
        // byte[] eventBeginTimeBytes = reader.ReadTimeRealBytes();
        // byte[] eventEndTimeBytes = reader.ReadTimeRealBytes();
        EventVehicleRegistration eventVehicleRegistration = ReadVehicleRegistrationIdentification(reader);

       /* if (eventType == 0 && eventBeginTimeBytes.All(b => b == 0) || eventEndTimeBytes.All(b => b == 0))
        return null;*/

        cardEventRecordsInfo cardEventRecord = new cardEventRecordsInfo(eventType,reader.ReadTimeReal().toString(),reader.ReadTimeReal().toString(),eventVehicleRegistration);

        return cardEventRecord;
    }

    public  static EventVehicleRegistration ReadVehicleRegistrationIdentification(BinaryReader reader)
    {
        EventVehicleRegistration vehicleRegistrationIdentification = new EventVehicleRegistration(reader.readByte(),ReadVehicleRegistrationNumber(reader));
        return vehicleRegistrationIdentification;
    }

    public static ControlVehicleRegistration ReadVehicleRegistration(BinaryReader reader)
    {
        ControlVehicleRegistration vehicleRegistrationIdentification = new ControlVehicleRegistration(reader.readByte(),ReadVehicleRegistrationNumber(reader));
        return vehicleRegistrationIdentification;
    }

    public static SessionOpenVehicle ReadSessionOpenVehicle(BinaryReader reader)
    {
        SessionOpenVehicle vehicleRegistrationIdentification = new SessionOpenVehicle(reader.readByte(),ReadVehicleRegistrationNumber(reader));
        return vehicleRegistrationIdentification;
    }

    public static String ReadVehicleRegistrationNumber(BinaryReader reader)
    {
        String vehicleRegistrationNumber = null;
        try {
            vehicleRegistrationNumber = ReadIA5String(reader,14);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vehicleRegistrationNumber;
    }

    public static DrivingLicenceInfo ReadDrivingLicenceInfo(BinaryReader reader)
    {
        DrivingLicenceInfo drivingLicenceInfo = new DrivingLicenceInfo(ReadCardDrivingLicenceInformation(reader));
        return drivingLicenceInfo;
    }

    public static CardDownload ReadCardDownload(BinaryReader reader)
    {
        CardDownload cardDownload = new CardDownload();
        //SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        //format.setTimeZone(TimeZone.getTimeZone("GMT"));
        cardDownload.setLastCardDownload(reader.ReadTimeReal().toString());
        return cardDownload;
    }

    public static CardDrivingLicenceInformation ReadCardDrivingLicenceInformation(BinaryReader reader)
    {
        CardDrivingLicenceInformation drivingLicenceInformation = null;
        try {
            drivingLicenceInformation = new CardDrivingLicenceInformation(ReadIA5String(reader,36),reader.readByte(),ReadIA5String(reader,16));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drivingLicenceInformation;
    }
    public static enum EquipmentType {
        Reserved("Reserved",(byte)0),
        DriverCard("DriverCard",(byte)1),
        WorkshopCard("WorkshopCard",(byte)2),
        ControlCard("ControlCard",(byte)3),
        CompanyCard("CompanyCard",(byte)4),
        ManufacturingCard("ManufacturingCard",(byte)5),
        VehicleUnit("VehicleUnit",(byte)6),
        MotionSensor("VehicleUnit",(byte)7),

        // G2:
        GnssFacility("GnssFacility",(byte)8),
        RemoteCommunicationModule("RemoteCommunicationModule",(byte)9),
        ItsInterfaceModule("ItsInterfaceModule",(byte)10),
        Plaque("Plaque",(byte)11),
        M1N1Adapter("M1N1Adapter",(byte)12),
        EuropeanRootCA("EuropeanRootCA",(byte)13),
        MemberStateCA("MemberStateCA",(byte)14),
        ExternalGnssConnection("ExternalGnssConnection",(byte)15),
        Unused("Unused",(byte)16);



        private final String name;
        private final byte point;

        public String getName() {
            return name;
        }

        public byte getPoint() {
            return point;
        }

        private EquipmentType(String name, byte point)
        {
            this.name = name;
            this.point = point;
        }
    }
    public static ApplicationIdentification ReadApplicationIdentification(BinaryReader reader)
    {
        ApplicationIdentification applicationIdentification = new ApplicationIdentification();
        applicationIdentification.driverCardApplicationIdentification = ReadDriverCardApplicationIdentification(reader);
        return applicationIdentification;
    }

    public static DriverCardApplicationIdentification ReadDriverCardApplicationIdentification(BinaryReader reader)
    {
        DriverCardApplicationIdentification cardApplicationIdentification = new DriverCardApplicationIdentification();

        cardApplicationIdentification.setTypeOfTachographCardId(ReadEquipmentType(reader).getName());
        cardApplicationIdentification.setCardStructureVersion(new CardStructureVersion(reader.readByte(),reader.readByte()));
        cardApplicationIdentification.setNoOfEventsPerType(reader.readByte());
        cardApplicationIdentification.setNoOfFaultsPerType(reader.readByte());
     /*   Word word = new Word(reader.readByte(),reader.readByte());
        ByteBuffer wrapped = ByteBuffer.wrap(word.ToBytes());
        cardApplicationIdentification.setActivityStructureLength(wrapped.getInt());
        reader.readByte();
        reader.readByte();
        Word word2 = new Word(reader.readByte(),reader.readByte());
        ByteBuffer wrapped2 = ByteBuffer.wrap(word2.ToBytes());
        cardApplicationIdentification.setNoOfCardVehicleRecords(wrapped2.getInt());*/
        cardApplicationIdentification.setActivityStructureLength(reader.readWordBE());
        cardApplicationIdentification.setNoOfCardVehicleRecords(reader.readWordBE());
        cardApplicationIdentification.setNoOfCardPlaceRecords(reader.readByte());

        return cardApplicationIdentification;
    }

    public static EquipmentType ReadEquipmentType(BinaryReader reader)
    {
        byte typeId = reader.readByte();
        EquipmentType equipmentType = ToEquipmentType(typeId);
        return equipmentType;
    }

    public static EquipmentType ToEquipmentType(byte equipmentTypeByte)
    {
        for (EquipmentType equipmentType : EquipmentType.values()) {
            if (equipmentType.getPoint() == equipmentTypeByte) {
                return equipmentType;
            }
        }
        return null;
    }

    public static CardNumber ReadCardNumber(BinaryReader reader) throws IOException {
        CardNumber cardNumber = new CardNumber(ReadIA5String(reader,14),ReadIA5String(reader,1),ReadIA5String(reader,1) );
        return cardNumber;

    }

    public static CardIccIdentification ReadCardIccIdentification(BinaryReader reader)
    {
        CardIccIdentification cardIccIdentification = new CardIccIdentification();

        try {
            cardIccIdentification.setClockStop(reader.readByte());
            cardIccIdentification.setCardExtendedSerialNumber(ReadExtendedSerialNumber(reader));
            cardIccIdentification.setCardApprovalNumber(ReadCardApprovalNumber(reader));
            cardIccIdentification.setCardPersonnaliserID(reader.readByte());
            cardIccIdentification.setEmbedderIcAssemblerId(ReadEmbedderIcAssemblerId(reader));
            cardIccIdentification.setIcIdentifier(reader.readBytes(2).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        return cardIccIdentification;
    }

    public static EmbedderIcAssemblerId ReadEmbedderIcAssemblerId(BinaryReader reader) throws IOException {
        EmbedderIcAssemblerId embedderIcAssemblerId = new EmbedderIcAssemblerId(reader.readBytes(2).toString(),reader.readBytes(2).toString(),reader.readByte());
        return embedderIcAssemblerId;
    }

    public static CardExtendedSerialNumber ReadExtendedSerialNumber(BinaryReader reader)
    {
        CardExtendedSerialNumber extendedSerialNumber = new CardExtendedSerialNumber();
        extendedSerialNumber.setSerialNumber(ToUInt32(reader.readBytes(4)));
        extendedSerialNumber.setMonth(reader.readByte());
        extendedSerialNumber.setYear(reader.readByte());
        extendedSerialNumber.setType(reader.readByte());
        extendedSerialNumber.setManufacturerCode(reader.readByte());

        return extendedSerialNumber;
    }


    public static String ReadCardApprovalNumber(BinaryReader reader) throws IOException {


        String cardApprovalNumber = //new String(reader.readBytes(8), StandardCharsets.ISO_8859_1);
                ReadIA5String(reader,8);
        return cardApprovalNumber;

    }

    private static char StartOfHeading = '\u0001';

    public static String ReadIA5String(BinaryReader reader, int length) throws IOException {
        byte[] strBytes = reader.readBytes(length);
        String correctString = null;
        correctString = new String(strBytes, StandardCharsets.ISO_8859_1);
        assert correctString.charAt(1) == '\u00E4'; //verify that the character was correctly decoded.
        int startOfHeadingIndex = correctString.indexOf(StartOfHeading);
        if (startOfHeadingIndex != -1)
            correctString = correctString.substring(startOfHeadingIndex + 1);
        String str = correctString.trim();
        return str;
    }


    public static CardChipIdentification ReadCardChipIdentification(BinaryReader reader)
    {

        CardChipIdentification cardChipIdentification = new CardChipIdentification (bytesToHex(reader.readBytes(4)),bytesToHex(reader.readBytes(4)));
        return cardChipIdentification;
    }

    public static boolean[] byteArray2BitArray(byte[] bytes) {
        boolean[] bits = new boolean[bytes.length * 8];
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[i / 8] & (1 << (7 - (i % 8)))) > 0)
                bits[i] = true;
        }
        return bits;
    }

    public static CardVehiclesUsed ReadCardVehiclesUsed(BinaryReader reader, int noOfCardVehicleRecords)
    {
        CardVehiclesUsed cardVehiclesUsed = new CardVehiclesUsed();
        cardVehiclesUsed.setVehiclePointerNewestRecord(reader.readWordBE());
        int cardVehicleRecordLength = 31;
        int vehicleRecordsBytesCount = noOfCardVehicleRecords  * cardVehicleRecordLength;
        byte[] vehicleRecordsBytes = reader.readBytes(vehicleRecordsBytesCount);

        cardVehiclesUsed.setCardVehicleRecords(GetCardVehicleRecords(vehicleRecordsBytes, noOfCardVehicleRecords));

        return cardVehiclesUsed;
    }

    private static RealmList<CardVehicleRecords> GetCardVehicleRecords(byte[] vehicleRecordsBytes, int vehiclePointerNewestRecord)
    {
        RealmList<CardVehicleRecords> records = new RealmList<CardVehicleRecords>();

        if (vehicleRecordsBytes.length > 0)
        {
            BinaryReader r = new BinaryReader(vehicleRecordsBytes);

                for (int i = 0; i < vehiclePointerNewestRecord; i++)
                {
                    CardVehicleRecords cardVehicleRecord = ReadCardVehicleRecord(r);
                    records.add(cardVehicleRecord);
                }

        }

        RealmList<CardVehicleRecords> vehicleRecords = records;
        return vehicleRecords;
    }

    private static CardVehicleRecords ReadCardVehicleRecord(BinaryReader reader)
    {
        CardVehicleRecords cardVehicleRecord = new CardVehicleRecords();
        cardVehicleRecord.setVehicleOdometerBegin(ReadOdometerShort(reader));
        cardVehicleRecord.setVehicleOdometerEnd(ReadOdometerShort(reader));
        cardVehicleRecord.setVehicleFirstUse(reader.ReadTimeReal().toString());
        cardVehicleRecord.setVehicleLastUse(reader.ReadTimeReal().toString());
        cardVehicleRecord.setVehicleRegistration(ReadVehicleRegistrationInfo(reader));
        cardVehicleRecord.setVuDataBlockCounter(reader.readBytes(2).toString());

        return cardVehicleRecord;
    }

    public static VehicleRegistration ReadVehicleRegistrationInfo(BinaryReader reader)
    {
        VehicleRegistration vehicleRegistrationIdentification = new VehicleRegistration();

        vehicleRegistrationIdentification.setVehicleRegistrationNation(reader.readByte());
        vehicleRegistrationIdentification.setVehicleRegistrationNumber(ReadVehicleRegistrationNumber(reader));

        return vehicleRegistrationIdentification;
    }

    public static int ReadOdometerShort(BinaryReader reader)
    {
        byte[] bytes = reader.readBytes(3);
        int odometerShort = ToUInt32(bytes);
        return odometerShort;
    }

    ///////////////////////////////////
    //// TACHO G2
    //////////////////////////////////


}
