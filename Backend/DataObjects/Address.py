from DataObjects.DatabaseObject import DatabaseObject
from decimal import Decimal, ROUND_DOWN
from FunctionalUtils import getLatitudeLongitude

class Address(DatabaseObject):
    TABLE_NAME = "ADDRESSES"
    TABLE_CREATE_STATEMENT = '''CREATE TABLE IF NOT EXISTS {0}(
          ID INT AUTO_INCREMENT,
          ownerEmail VARCHAR(100) NOT NULL,
          address VARCHAR(200) NOT NULL,
          spotType SMALLINT NOT NULL,
          isCovered BOOL NOT NULL,
          description VARCHAR(300),
          picturePath VARCHAR(300),
          FOREIGN KEY (ownerEmail) REFERENCES USERS(email),
          PRIMARY KEY (ID)
        );'''.format(TABLE_NAME)
    MILES_MAGIC = 3959
    SPOT_TYPES = {0 : 'motorcycle',
                  1 : 'compact',
                  2 : 'regular car',
                  3 : 'truck'}
    def __init__(self, address, spotType,
                 ownerEmail, isCovered,
                 description="", picturePath=""):
        '''
            :type address: str
            :type spotType: int
            :type ownerEmail: str
            :type isBooked: bool
            :type price: float
            :type start: str
            :type end: str
            :type isCovered: bool
            :type cancelationPolicy: int
            :type isRecurring: bool
            :type renterEmail: str or None
        '''
        assert spotType in self.SPOT_TYPES
        
        self.address           = str(address)
        self.spotType          = spotType
        self.ownerEmail        = str(ownerEmail)
        self.isCovered         = isCovered
        self.description       = str(description)
        self.picturePath       = str(picturePath)

    def __iter__(self):
        return iter([('address'          , self.address),
                     ('spotType'         , self.spotType),
                     ('ownerEmail'       , self.ownerEmail),
                     ('isCovered'        , self.isCovered),
                     ('picturePath'      , self.picturePath),
                     ('description'    , self.description)])

    @classmethod
    def addPicture(cls, path, ownerEmail, address):
        '''
        :type email: str
        :type path: str
        :rtype: str
        '''
        return '''UPDATE {0} SET picturePath=\'{1}\' WHERE ownerEmail=\'{2}\' AND address=\'{3}\';'''.format(cls.TABLE_NAME, path, ownerEmail, address)


    @classmethod
    def deleteAddress(cls, ownerEmail, spotID):
        return '''DELETE FROM ADDRESSES where ID = {0} and ownerEmail = \'{1}\';'''.format(spotID, ownerEmail)


    @classmethod
    def updateSpotType(cls, addressID, spotType):
        return '''UPDATE {0} SET spotType = \'{1}\' WHERE ID = {2};'''.format(cls.TABLE_NAME, spotType, addressID)

    @classmethod
    def updateIsCovered(cls, addressID, isCovered):
        return '''UPDATE {0} SET isCovered = \'{1}\' WHERE ID = {2};'''.format(cls.TABLE_NAME, isCovered, addressID)

    @classmethod
    def updateDescription(cls, addressID, description):
        return '''UPDATE {0} SET description = \'{1}\' WHERE ID = {2};'''.format(cls.TABLE_NAME, description, addressID)

    @classmethod
    def viewAddressInfo(cls, addressID):
        return '''SELECT ownerEmail, address, spotType, isCovered, description FROM ADDRESSES WHERE ID = {0};'''.format(addressID)

    @classmethod
    def getPicturePath(cls, addressID):
        return '''SELECT picturePath FROM ADDRESSES WHERE ID = {0};'''.format(addressID)

    @classmethod
    def searchByOwnerEmailQuery(cls, ownerEmail):
        return '''SELECT ID, address FROM {0} WHERE ownerEmail=\'{1}\';'''.format(cls.TABLE_NAME, ownerEmail)

    @classmethod
    def getCount(cls, addressID):
        return '''SELECT COUNT(*) FROM SPOTS WHERE addressID=\'{0}\' AND isBooked=\'{1}\';'''.format(addressID, 1)

