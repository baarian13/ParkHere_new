'''
Created on Oct 19, 2016

@author: henrylevy
'''
from Model.BaseManagers.DatabaseManagerBase import SQLDatabaseManager
from DataObjects.Spot import Spot
from FunctionalUtils import getLatitudeLongitude

class SQLSpotDatabaseManager(SQLDatabaseManager):
    DB_OBJECT_CLASS = Spot

    def __init__(self, host, user, password, port, db):
        '''
        :type host: str
        :type user: str
        :type password: str
        :type port: int
        :type db: str
        '''
        super(SQLSpotDatabaseManager, self).__init__(host, user, password,
                                                 port, db, Spot)
    
    def getSpotsRentedBy(self, userEmail):
        '''
        :type userEmail: str
        :rtype: list
        '''
        self.cursor.execute(Spot.searchByRenterEmailQuery(userEmail))
        return self.cursor.fetchall()
    
    def getSpotIDsRentedBy(self, userEmail):
        '''
        :type userEmail: str
        :rtype: list
        '''
        self.cursor.execute(Spot.searchIDByRenterEmailQuery(userEmail))
        return self.cursor.fetchall()

    def getSpotHistoryOf(self, userEmail):
        '''
        :type userEmail: str
        :rtype: list
        '''
        self.cursor.execute(Spot.searchHistoryIDByRenterEmailQuery(userEmail))
        return self.cursor.fetchall()

    def getSpotHistoryIDsRentedBy(self, userEmail):
        '''
        :type userEmail: str
        :rtype: list
        '''
        self.cursor.execute(Spot.searchHistoryIDByRenterEmailQuery(userEmail))
        return self.cursor.fetchall()
    
    def getSpotsOwnedBy(self, userEmail):
        '''
        :type userEmail: str
        :rtype: list
        '''
        self.cursor.execute(Spot.searchByOwnerEmailQuery(userEmail))
        return self.cursor.fetchall()

    def getSpotIDsOwnedBy(self, userEmail):
        '''
        :type userEmail: str
        :rtype: list
        '''
        self.cursor.execute(Spot.searchIDByOwnerEmailQuery(userEmail))
        return self.cursor.fetchall()

    def searchForSpots(self, address, maxDistance=25, maxResults=20):
        '''
        :type address: str
        :type maxDistance: int
        :type maxResults: int
        :rtype: list
        '''
        latitude, longitude = getLatitudeLongitude(address)
        self.cursor.execute(Spot.searchByDistanceQuery(latitude, longitude, maxDistance, maxResults))
        return self.cursor.fetchall()
    
    def viewSpotInfo(self, spotID):
        self.cursor.execute(Spot.viewSpotInfo(spotID))
        info = self.cursor.fetchall()[0]
        # self.cursor.execute(Spot.getPicturePath(spotID))
        # picturePath = self.cursor.fetchall()
        # info.append(self.objStorageManager.downloadPictureAsString(picturePath))
        return info

    def bookSpot(self, renterEmail, spotID):
        self.execute(Spot.bookSpot(renterEmail, spotID, False))

    def deleteSpot(self, ownerEmail, spotID):
        self.execute(Spot.deleteSpot(ownerEmail, spotID))

    def cancelReservation(self, spotID):
        self.execute(Spot.cancelReservation(spotID))
