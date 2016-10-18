'''
Created on Oct 17, 2016

@author: henrylevy
'''
from datetime import date
from DataObjects.DatabaseObject import DatabaseObject
from decimal import Decimal, ROUND_DOWN

class Spot(DatabaseObject):
    TABLE_NAME = "SPOTS"
    TABLE_CREATE_STATEMENT = '''CREATE TABLE IF NOT EXISTS {0}(
                    ID INT AUTO_INCREMENT,
                    renterID INT NOT NULL,
                    ownerID INT NOT NULL,
                    price DECIMAL(10, 2) NOT NULL,
                    address VARCHAR(200) NOT NULL,
                    spotType SMALLINT NOT NULL,
                    isBooked BOOL NOT NULL,
                    startDate DATE NOT NULL,
                    endDate DATE NOT NULL,
                    FOREIGN KEY (renterID) REFERENCES USERS(ID),
                    FOREIGN KEY (ownerID) REFERENCES USERS(ID),
                    PRIMARY KEY (ID));'''.format(TABLE_NAME)
    
    SPOT_TYPES = {0 : "Covered",
                  1 : "Open"} # TODO: do we need more?
    
    def __init__(self, renterUserID, address, spotType,
                 ownerID, isBooked, price,
                 startDate, endDate, spotID):
        '''
            :type renterUserID: int
            :type address: str
            :type spotType: int
            :type ownerID: int
            :type isBooked: bool
            :type price: float
            :type startDate: date
            :type endDate: date
        '''
        assert spotType in self.SPOT_TYPES
        
        self.renterUserID = renterUserID
        self.address = address
        self.spotType = spotType
        self.ownerID = ownerID
        self.isBooked = isBooked
        self.price = Decimal(price).quantize(Decimal('.01'), rounding=ROUND_DOWN)
        self.startDate = startDate
        self.endDate = endDate
        self.spotID = spotID
    
    def isValidSpot(self): 
        return self.startDate < date.today() < self.endDate
    
    def getSpotType(self):
        assert self.spotType in self.SPOT_TYPES
        return self.SPOT_TYPES.get(self.spotType)

    def asInsertStatement(self):
        startDate = "{0}-{1}-{2}".format(self.startDate.year,
                                         self.startDate.month,
                                         self.startDate.day)
        endDate = "{0}-{1}-{2}".format(self.endDate.year,
                                       self.endDate.month,
                                       self.endDate.day)
        return """INSERT INTO {0} 
        (renterID, ownerID, price, address, spotType, isBooked, startDate, endDate) 
        VALUES ({1}, {2}, {3}, {4}, {5}, {6}, {7}, {8});
        """.format(self.TABLE_NAME, self.renterUserID, self.ownerID, str(self.price),
                   self.address, self.spotType, self.isBooked, startDate, endDate)
