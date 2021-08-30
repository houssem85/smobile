package fr.strada.smobile.utils.cardlib;

import java.util.ArrayList;
import java.util.List;

import fr.strada.smobile.utils.cardlib.models.ApplicationIdentificationTG2;
import fr.strada.smobile.utils.cardlib.models.CardStructureVersion;
import fr.strada.smobile.utils.cardlib.models.DriverCardApplicationIdentificationG2;
import fr.strada.smobile.utils.cardlib.models.GeoCoordinates;
import fr.strada.smobile.utils.cardlib.models.GnssContinuousDriving;
import fr.strada.smobile.utils.cardlib.models.GnssContinuousDrivingRecords;
import fr.strada.smobile.utils.cardlib.models.GnssPlaceRecord;
import fr.strada.smobile.utils.cardlib.models.GnssPlaces;

import static fr.strada.smobile.utils.cardlib.DataParsing.ReadEquipmentType;

public class DataParsingG2 {

    public static DriverCardApplicationIdentificationG2 ReadDriverCardApplicationIdentification(BinaryReader reader)
    {
        DriverCardApplicationIdentificationG2 cardApplicationIdentification = new DriverCardApplicationIdentificationG2();

        cardApplicationIdentification.setTypeOfTachographCardId(ReadEquipmentType(reader).getName());
        cardApplicationIdentification.setCardStructureVersion(new CardStructureVersion(reader.readByte(),reader.readByte()));
        cardApplicationIdentification.setNoOfEventsPerType(reader.readByte());
        cardApplicationIdentification.setNoOfFaultsPerType(reader.readByte());
        cardApplicationIdentification.setActivityStructureLength(reader.readWordBE());
        cardApplicationIdentification.setNoOfCardVehicleRecords(reader.readWordBE());
        cardApplicationIdentification.setNoOfCardPlaceRecords(reader.readByte());
        cardApplicationIdentification.setNoOfGNSSCDRecords(reader.readWord());
        cardApplicationIdentification.setNoOfSpecificConditionRecords(reader.readWord());

        return cardApplicationIdentification;
    }

    public static ApplicationIdentificationTG2 ReadApplicationIdentification(BinaryReader reader)
    {
        ApplicationIdentificationTG2 cardApplicationIdentification = new ApplicationIdentificationTG2();
        cardApplicationIdentification.setDriverCardApplicationIdentification(ReadDriverCardApplicationIdentification(reader));
        return cardApplicationIdentification;
    }

    public static GnssPlaces ReadGnssPlaces(BinaryReader reader, int noOfGNSSCDRecords)
    {
        GnssPlaces gnssPlaces = new GnssPlaces(ReadGnssContinuousDriving(reader, noOfGNSSCDRecords));
        return gnssPlaces;
    }
    public static GnssContinuousDriving ReadGnssContinuousDriving(BinaryReader reader, int noOfGNSSCDRecords)
    {
        GnssContinuousDriving gnssContinuousDriving = new GnssContinuousDriving();
        gnssContinuousDriving.setGnssCDPointerNewestRecord(reader.readWordBE());
        gnssContinuousDriving.setGnssContinuousDrivingRecords(ReadContinuousDrivingRecords(reader, noOfGNSSCDRecords));
        return gnssContinuousDriving;
    }
    public static List<GnssContinuousDrivingRecords> ReadContinuousDrivingRecords(BinaryReader reader, int noOfGNSSCDRecords)
    {
        List<GnssContinuousDrivingRecords> records = new ArrayList<>();

        for (int i = 0; i < noOfGNSSCDRecords; i++)
        {
            GnssContinuousDrivingRecords record = ReadContinuousDrivingRecord(reader);
            records.add(record);
        }

        List<GnssContinuousDrivingRecords> recordArray = records;
        return recordArray;
    }
    public static GnssContinuousDrivingRecords ReadContinuousDrivingRecord(BinaryReader reader)
    {
        GnssContinuousDrivingRecords record = new GnssContinuousDrivingRecords();
        record.setTimestamp(reader.ReadTimeReal().toString());
        record.setGnssPlaceRecord(ReadGnssPlaceRecord(reader));
        return record;
    }
    public static GnssPlaceRecord ReadGnssPlaceRecord(BinaryReader reader)
    {
        GnssPlaceRecord record = new GnssPlaceRecord();
        record.setTimestamp(reader.ReadTimeReal().toString());
        record.setGnssAccuracy(reader.readByte());
        record.setGeoCoordinates(ReadGeoCoordinates(reader));
        return record;
    }
    public static GeoCoordinates ReadGeoCoordinates(BinaryReader reader)
    {
        GeoCoordinates record = new GeoCoordinates();
        record.setLatitude(ReadCoordinate(reader).toString());
        record.setLongitude(ReadCoordinate(reader).toString());
        return record;
    }

    private static byte[] ReadCoordinate(BinaryReader reader)
    {
        byte[] bytes = reader.readBytes(3);
        return bytes;
    }
}
