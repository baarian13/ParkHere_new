'''
Created on Oct 17, 2016

@author: henrylevy
'''
from datetime import date
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
                    startDate DATE NOT NULL,
                    endDate DATE NOT NULL,
                    latitude FLOAT NOT NULL,
                    longitude FLOAT NOT NULL,
                    isCovered BOOL NOT NULL,
                    cancelationPolicy SMALLINT NOT NULL,
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
                 startDate, endDate, 
                 isCovered, cancelationPolicy,
                 renterEmail=None):
        '''
            :type address: str
            :type spotType: int
            :type ownerEmail: str
            :type isBooked: bool
            :type price: float
            :type startDate: date
            :type endDate: date
            :type isCovered: bool
            :type cancelationPolicy: int
            :type renterEmail: str or None
        '''
        assert spotType in self.SPOT_TYPES
        assert cancelationPolicy in self.CANCELATION_POLICIES
        
        self.address           = address
        self.spotType          = spotType
        self.ownerEmail        = ownerEmail
        self.isBooked          = isBooked
        self.renterEmail       = renterEmail
        self.isCovered         = isCovered
        self.cancelationPolicy = cancelationPolicy
        self.price             = Decimal(price).quantize(Decimal('.01'), rounding=ROUND_DOWN)
        self.startDate         = startDate
        self.endDate           = endDate
        self.startDateStr      = "{0}-{1}-{2}".format(startDate.year,
                                                      startDate.month,
                                                      startDate.day)
        self.endDateStr        = "{0}-{1}-{2}".format(endDate.year,
                                                      endDate.month,
                                                      endDate.day)
        self.latitude, self.longitude = getLatitudeLongitude(self.address)
        
        self.data = {'address'           : address,
                     'spotType'          : spotType,
                     'ownerEmail'        : ownerEmail,
                     'isBooked'          : isBooked,
                     'price'             : self.price,
                     'startDate'         : self.startDateStrs,
                     'endDate'           : self.endDateStr,
                     'renterEmail'       : renterEmail,
                     'latitude'          : self.latitude,
                     'longitude'         : self.longitude,
                     'isCovered'         : isCovered,
                     'cancelationPolicy' : cancelationPolicy}
        if not self.renterEmail:
            self.data.pop('renterEmail')

    @classmethod
    def searchByDistanceQuery(cls, latitude, longitude, maxDistance=25, maxResults=20):
        return '''SELECT ID, address, startDate, endDate, 
    ({0} * acos( cos( radians({1}) ) * cos( radians( latitude) ) * cos( radians(longitude) - radians({2}) ) + sin( radians({1}) ) * sin( radians( latitude ) ) ) ) AS distance
    FROM SPOTS HAVING distance < {3} ORDER BY distance LIMIT 0 , {4};'''.format(cls.MILES_MAGIC, latitude, longitude, maxDistance, maxResults)

    @classmethod
    def searchByOwnerEmailQuery(cls, ownerEmail):
        return '''SELECT ID, address, startDate, endDate, FROM SPOTS WHERE ownerEmail = {0};'''.format(ownerEmail)

    @classmethod
    def searchByRenterEmailQuery(cls, renterEmail):
        return '''SELECT ID, address, startDate, endDate, FROM SPOTS WHERE renterEmail = {0};'''.format(renterEmail)

    def isValidSpot(self): 
        return self.startDate < date.today() < self.endDate

