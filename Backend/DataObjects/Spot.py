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
                    FOREIGN KEY (renterEmail) REFERENCES USERS(email),
                    FOREIGN KEY (ownerID) REFERENCES USERS(email),
                    PRIMARY KEY (ID));'''.format(TABLE_NAME)
    MILES_MAGIC = 3959
    SPOT_TYPES = {0 : "Covered",
                  1 : "Open"} # TODO: do we need more?
    
    def __init__(self, address, spotType,
                 ownerEmail, isBooked, price,
                 startDate, endDate, renterEmail=None):
        '''
            :type address: str
            :type spotType: int
            :type ownerEmail: str
            :type isBooked: bool
            :type price: float
            :type startDate: date
            :type endDate: date
            :type renterEmail: str or None
        '''
        assert spotType in self.SPOT_TYPES
        
        self.address = address
        self.spotType = spotType
        self.ownerEmail = ownerEmail
        self.isBooked = isBooked
        self.price = Decimal(price).quantize(Decimal('.01'), rounding=ROUND_DOWN)
        self.startDate = startDate
        self.endDate = endDate
        self.renterEmail = renterEmail
        self.latitude, self.longitude = getLatitudeLongitude(self.address)

    @classmethod
    def searchByDistanceQuery(cls, latitude, longitude, maxDistance=25, maxResults=20):
        return '''SELECT ID, address, startDate, endDate, 
    ({0} * acos( cos( radians({1}) ) * cos( radians( latitude) ) * cos( radians(longitude) - radians({2}) ) + sin( radians({1}) ) * sin( radians( latitude ) ) ) ) AS distance
    FROM SPOTS HAVING distance < {3} ORDER BY distance LIMIT 0 , {4};'''.format(cls.MILES_MAGIC, latitude, longitude, maxDistance, maxResults)


    def isValidSpot(self): 
        return self.startDate < date.today() < self.endDate
    
    def getSpotType(self):
        assert self.spotType in self.SPOT_TYPES
        return self.SPOT_TYPES.get(self.spotType)

    def asInsertStatement(self):
        startDate = "{0}-{1}-{2}".format(self.startDate.year,
                                         self.startDate.month,
                                         self.startDate.day)
        endDate   = "{0}-{1}-{2}".format(self.endDate.year,
                                         self.endDate.month,
                                         self.endDate.day)
        params = '''ownerEmail, price, address, spotType, isBooked, startDate, endDate, latitude, longitude'''
        values = '''{0}, {1}, {2}, {3}, {4}, {5}, {6}'''.format(self.ownerEmail, str(self.price),
            self.address, self.spotType, self.isBooked, startDate, endDate, self.latitude, self.longitude)
        if self.renterEmail:
            params += ', renterEmail'
            params += ', {0}'.format(self.renterEmail)
        return """INSERT INTO {0} ({1}) VALUES ({2});""".format(self.TABLE_NAME, params, values)

