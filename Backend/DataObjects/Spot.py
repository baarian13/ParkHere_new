'''
Created on Oct 17, 2016

@author: henrylevy
'''
from datetime import date
from DataObjects.DatabaseObject import DatabaseObject

class Spot(DatabaseObject):
    def __init__(self, renterUserID, address, spotType,
                 ownerID, pitureIDs, isBooked, price,
                 startDate, endDate):
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
        self.renterUserID = renterUserID
        self.address = address
        self.spotType = spotType
        self.ownerID = ownerID
        self.isBooked = isBooked
        self.price = price
        self.startDate = startDate
        self.endDate = endDate
    
    def isValidSpot(self): 
        return self.startDate < date.today() < self.endDate
    