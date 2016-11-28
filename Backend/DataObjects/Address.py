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
					latitude FLOAT NOT NULL,
					longitude FLOAT NOT NULL,
					FOREIGN KEY (ownerEmail) REFERENCES USERS(email),
					PRIMARY KEY (ID)
				);'''.format(TABLE_NAME)
	MILES_MAGIC = 3959
    SPOT_TYPES = {0 : 'motorcycle',
                  1 : 'compact',
                  2 : 'regular car',
                  3 : 'truck'}
     def __init__(self, address, spotType,
                 ownerEmail, isCovered, addressID,
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
        assert cancelationPolicy in self.CANCELATION_POLICIES
        
        self.address           = str(address)
        self.spotType          = spotType
        self.ownerEmail        = str(ownerEmail)
        self.isCovered         = isCovered
        self.description       = description
        self.picturePath       = str(picturePath)\
        self.latitude, self.longitude = getLatitudeLongitude(self.address)

     def __iter__(self):
        return iter([('address'          , self.address),
                     ('spotType'         , self.spotType),
                     ('ownerEmail'       , self.ownerEmail),
                     ('latitude'         , self.latitude),
                     ('longitude'        , self.longitude),
                     ('isCovered'        , self.isCovered),
                     ('picturePath'      , self.picturePath),
                     ('addressID'        , self.addressID),
                     ('description'		 , self.description)])

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