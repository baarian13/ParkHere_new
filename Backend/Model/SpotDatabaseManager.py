'''
Created on Oct 19, 2016

@author: henrylevy
'''
from Model.BaseManagers.DatabaseManagerBase import SQLDatabaseManager
from Model.BaseManagers.ObjectStorageManager import ObjectStorageManager
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

    def getSpotHistoryRentedBy(self, userEmail):
        '''
        :type userEmail: str
        :rtype: list
        '''
        self.cursor.execute(Spot.searchHistoryByRenterEmailQuery(userEmail))
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

    def searchForSpotsDateTime(self, start, end, maxResults=20):
        '''
        :type address: str
        :type maxDistance: int
        :type maxResults: int
        :rtype: list
        '''
        self.cursor.execute(Spot.searchByTimeQuery(start, end, maxResults))
        return self.cursor.fetchall()

    def searchForSpotsLocationAndDate(self, address, start, end, maxDistance=25, maxResults=20):
        '''
        :type address: str
        :type maxDistance: int
        :type maxResults: int
        :rtype: list
        '''
        latitude, longitude = getLatitudeLongitude(address)
        self.cursor.execute(Spot.searchByDisAndTimeQuery(latitude, longitude, start, end, maxDistance, maxResults))
        return self.cursor.fetchall()

    def viewSpotInfo(self, spotID):
        self.cursor.execute(Spot.viewSpotInfo(spotID))
        info = list(self.cursor.fetchall()[0])
        try:
            self.cursor.execute(Spot.getPicturePath(spotID))
            picturePath = self.cursor.fetchall()[0][0]
            print picturePath
            info.append(self.objStorageManager.downloadPictureAsB64(picturePath))
        except Exception as e:
            print e
        return info

    def bookSpot(self, renterEmail, spotID):
        self.execute(Spot.bookSpot(renterEmail, spotID, 1))

    def deleteSpot(self, ownerEmail, spotID):
        self.execute(Spot.deleteSpot(ownerEmail, spotID))

    def cancelReservation(self, spotID):
        self.execute(Spot.cancelReservation(spotID))

    def submitPicture(self, pictureString, ownerEmail, address):
        '''
        :type email: str
        :type pictureString: str
        '''
        print 'submit picture'
        path = 'spotPictures/{0}'.format((ownerEmail+address).replace(" ", ""))
        print path
        self.objStorageManager.uploadMedia(path, pictureString)
        print 'succesful media upload'
        self.execute(Spot.addPicture(path, ownerEmail, address))

    def rateSpot(self, spotId, rating):
        self.cursor.execute(Spot.getRating(spotId))
        results = self.cursor.fetchall()[0]
        oldrating = results[0]
        numReviews = results[1]
        print "here1"
        print 'rating ' +rating
        rating = (int(oldrating)) * (int(numReviews)) + int(rating)
        print "here.5"
        numReviews = int(numReviews) + 1
        print "here2"

        rating = float(rating)/float(numReviews)
        print "here2"
        print rating
        print numReviews
        self.execute(Spot.setRating(spotId, rating, numReviews))
        self.cursor.execute('''SELECT rating FROM SPOTS WHERE id = \'1\';''')
        print self.cursor.fetchall()[0][0]

    def updatePrice(self, spotID, price):
        self.execute(Spot.modifyPrice(spotID, price))

    def modifyCancelationPolicy(self, spotID, cPolicy):
        self.execute(Spot.modifyCancellationPolicy(spotID, cPolicy));

    def getOwnerEmail(self, spotID):
        self.cursor.execute('''SELECT ownerEmail FROM SPOTS WHERE ID = \'{0}\';'''.format(spotID))
        return self.cursor.fetchall()[0][0]
