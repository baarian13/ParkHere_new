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
        self.cursor.execute(Spot.searchByRenterEmailQuery(userEmail))
        return self.cursor.fetchall()
    
    def getSpotsOwnedBy(self, userEmail):
        self.cursor.execute(Spot.searchByOwnerEmailQuery(userEmail))
        return self.cursor.fetchall()

    def searchForSpots(self, address, maxDistance=25, maxResults=20):
        latitude, longitude = getLatitudeLongitude(address)
        self.cursor.execute(Spot.searchByDistanceQuery(latitude, longitude, maxDistance, maxResults))
        return self.cursor.fetchall()
        