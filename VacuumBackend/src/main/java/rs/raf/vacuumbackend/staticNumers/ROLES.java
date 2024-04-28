package rs.raf.vacuumbackend.staticNumers;

public class ROLES {
    public static final int CAN_READ_USERS = 0b1;
    public static final int CAN_CREATE_USERS = 0b10;
    public static final int CAN_UPDATE_USERS = 0b100;
    public static final int CAN_DELETE_USERS = 0b1000;
    public static final int CAN_SEARCH_VACUUM = 0b10000;
    public static final int CAN_START_VACUUM = 0b100000;
    public static final int CAN_STOP_VACUUM = 0b1000000;
    public static final int CAN_DISCHARGE_VACUUM = 0b10000000;
    public static final int CAN_ADD_VACUUM = 0b100000000;
    public static final int CAN_REMOVE_VACUUM = 0b1000000000;
}
