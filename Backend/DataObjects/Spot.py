'''
Created on Oct 17, 2016

@author: henrylevy
'''
from datetime import date, datetime
import time
from DataObjects.DatabaseObject import DatabaseObject
from decimal import Decimal, ROUND_DOWN
from FunctionalUtils import getLatitudeLongitude

class Spot(DatabaseObject):
    TABLE_NAME = "SPOTS"
    TABLE_CREATE_STATEMENT = '''CREATE TABLE IF NOT EXISTS {0}(
                    ID INT AUTO_INCREMENT,
                    renterEmail VARCHAR(100),
                    ownerEmail VARCHAR(100) NOT NULL,
                    price DECIMAL(10, 2) NOT NULL,
                    address VARCHAR(200) NOT NULL,
                    spotType SMALLINT NOT NULL,
                    isBooked BOOL NOT NULL,
                    start DATETIME NOT NULL,
                    end DATETIME NOT NULL,
                    latitude FLOAT NOT NULL,
                    longitude FLOAT NOT NULL,
                    isCovered BOOL NOT NULL,
                    isRecurring BOOL NOT NULL,
                    description VARCHAR(300),
                    picturePath VARCHAR(300),
                    cancelationPolicy SMALLINT NOT NULL,
                    numReviews INT NOT NULL,
                    rating FLOAT NOT NULL,
                    FOREIGN KEY (renterEmail) REFERENCES USERS(email),
                    FOREIGN KEY (ownerEmail) REFERENCES USERS(email),
                    PRIMARY KEY (ID));'''.format(TABLE_NAME)
    MILES_MAGIC = 3959
    SPOT_TYPES = {0 : 'motorcycle',
                  1 : 'compact',
                  2 : 'regular car',
                  3 : 'truck'}
    CANCELATION_POLICIES = {0 : 'flexible',
                            1 : 'moderate',
                            2 : 'strict'}
    
    def __init__(self, address, spotType,
                 ownerEmail, isBooked, price,
                 start, end, isCovered,
                 cancelationPolicy, isRecurring,
                 description="", picturePath="", renterEmail=None, numReviews=0, rating=0,):
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
            :type numReviews: int
            :type rating: int
        '''
        assert spotType in self.SPOT_TYPES
        assert cancelationPolicy in self.CANCELATION_POLICIES
        
        self.address           = str(address)
        self.spotType          = spotType
        self.ownerEmail        = str(ownerEmail)
        self.isBooked          = isBooked
        self.renterEmail       = str(renterEmail or "")
        self.isCovered         = isCovered
        self.cancelationPolicy = cancelationPolicy
        self.price             = str(Decimal(price).quantize(Decimal('.01'), rounding=ROUND_DOWN))
        self.start             = datetime.strptime(start, '%Y-%m-%d %H:%M:%S')
        self.end               = datetime.strptime(end,   '%Y-%m-%d %H:%M:%S')
        self.startStr          = str(start)
        self.endStr            = str(end)
        self.isRecurring       = isRecurring
        self.description       = description
        self.picturePath       = str(picturePath)
        self.latitude, self.longitude = getLatitudeLongitude(self.address)
        self.numReviews         = numReviews
        self.rating             = rating

    def __iter__(self):
        return iter([('address'          , self.address),
                     ('spotType'         , self.spotType),
                     ('ownerEmail'       , self.ownerEmail),
                     ('isBooked'         , self.isBooked),
                     ('price'            , self.price),
                     ('start'            , self.startStr),
                     ('end'              , self.endStr),
                     ('renterEmail'      , self.renterEmail),
                     ('latitude'         , self.latitude),
                     ('longitude'        , self.longitude),
                     ('isCovered'        , self.isCovered),
                     ('isRecurring'      , self.isRecurring),
                     ('cancelationPolicy', self.cancelationPolicy),
                     ('picturePath'      , self.picturePath),
                     ('numReviews'        , self.numReviews),
                     ('rating'            , self.rating)])


    @classmethod
    def searchByDistanceQuery(cls, latitude, longitude, maxDistance=25, maxResults=20):
        return '''SELECT ID, address, start, end, spotType, ownerEmail,
        renterEmail, isRecurring, isCovered, cancelationPolicy,
    ({0} * acos( cos( radians({1}) ) * cos( radians( latitude) ) * cos( radians(longitude) - radians({2}) ) + sin( radians({1}) ) * sin( radians( latitude ) ) ) ) AS distance
    FROM SPOTS HAVING distance < {3} ORDER BY distance LIMIT 0 , {4};'''.format(cls.MILES_MAGIC, latitude, longitude, maxDistance, maxResults)

    @classmethod
    def searchIDByRenterEmailQuery(cls, renterEmail):
        return '''SELECT ID FROM {0} WHERE renterEmail=\'{1}\' AND end >= \'{2}\';'''.format(cls.TABLE_NAME, renterEmail, time.strftime("%Y-%m-%d %H:%M:%S"))

    @classmethod
    def searchHistoryIDByRenterEmailQuery(cls, renterEmail):
        return '''SELECT ID FROM {0} WHERE renterEmail=\'{1}\' AND end < \'{2}\';'''.format(cls.TABLE_NAME, renterEmail, time.strftime("%Y-%m-%d %H:%M:%S"))

    @classmethod
    def bookSpot(cls, renterEmail, spotID, isBooked):
        return '''UPDATE {0} SET 
        isBooked=\'{1}\', renterEmail=\'{2}\' WHERE ID=\'{3}\';'''.format(cls.TABLE_NAME, isBooked, renterEmail, spotID)

    @classmethod
    def searchIDByOwnerEmailQuery(cls, ownerEmail):
        return '''SELECT ID FROM {0} WHERE ownerEmail=\'{1}\';'''.format(cls.TABLE_NAME, ownerEmail)

    @classmethod
    def getPicturePath(cls, spotID):
        return '''SELECT picturePath FROM SPOTS WHERE ID = {0};'''.format(spotID)

    @classmethod
    def viewSpotInfo(cls, spotID):
        return '''SELECT address, start, end, spotType, ownerEmail,
        renterEmail, isRecurring, isCovered, cancelationPolicy, description, price, picture, rating FROM SPOTS WHERE ID = {0};'''.format(spotID)

    @classmethod
    def searchByOwnerEmailQuery(cls, ownerEmail):
        return '''SELECT ID, address, start, end FROM SPOTS WHERE ownerEmail = \'{0}\';'''.format(ownerEmail)

    @classmethod
    def deleteSpot(cls, ownerEmail, spotID):
        return '''DELETE FROM SPOTS where ID = {0} and ownerEmail = \'{1}\';'''.format(spotID, ownerEmail)

    @classmethod
    def searchByRenterEmailQuery(cls, renterEmail):
        return '''SELECT ID, address, start, end FROM SPOTS WHERE renterEmail = {0} AND end >= \'{1}\';'''.format(renterEmail, time.strftime("%Y-%m-%d %H:%M:%S"))

    @classmethod
    def searchHistoryByRenterEmailQuery(cls, renterEmail):
        return '''SELECT ID, address, start, end FROM SPOTS WHERE renterEmail = {0} AND end < \'{1}\';'''.format(renterEmail, time.strftime("%Y-%m-%d %H:%M:%S"))

    @classmethod
    def cancelReservation(cls, spotID):
        return '''UPDATE {0} SET renterEmail=\'{1}\' WHERE ID=\'{2}\';'''.format(cls.TABLE_NAME, "", spotID)
    
    @classmethod
    def addPicture(cls, path, ownerEmail, address):
        '''
        :type email: str
        :type path: str
        :rtype: str
        '''
        return '''UPDATE {0} SET picturePath=\'{1}\' WHERE ownerEmail=\'{2}\' AND address=\'{3}\';'''.format(cls.TABLE_NAME, path, ownerEmail, address)
    def isValidSpot(self): 
        return self.start < date.today() < self.end


    @classmethod
    def getRating(cls, spotId):
        return '''SELECT rating, numReviews FROM {0}
                        WHERE ID = \'{1}\';'''.format(cls.TABLE_NAME, spotId)

    @classmethod
    def setRating(cls, spotId, rating, numReviews):
        return '''UPDATE {0} SET rating=\'{1}\' , numReviews = \'{2}\' WHERE ID=\'{3}\';'''.format(cls.TABLE_NAME, rating, numReviews, spotId)

