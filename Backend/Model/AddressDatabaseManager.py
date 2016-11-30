from Model.BaseManagers.DatabaseManagerBase import SQLDatabaseManager
from Model.BaseManagers.ObjectStorageManager import ObjectStorageManager
from DataObjects.Address import Address
'''Model > SpotDatabaseManager.py'''
'''wrapper of queries to the database: getters'''
class SQLAddressDatabaseManager(SQLDatabaseManager):
    DB_OBJECT_CLASS = Address

    def __init__(self, host, user, password, port, db):
        '''
        :type host: str
        :type user: str
        :type password: str
        :type port: int
        :type db: str
        '''
        super(SQLAddressDatabaseManager, self).__init__(host, user, password,
            port, db, Address)

    def submitPicture(self, pictureString, ownerEmail, address):
        '''
        :type email: str
        :type pictureString: str
        '''
        print 'submit picture'
        path = 'addressPictures/{0}'.format((ownerEmail+address).replace(" ", ""))
        print path
        self.objStorageManager.uploadMedia(path, pictureString)
        print 'succesful media upload'
        self.execute(Address.addPicture(path, ownerEmail, address))

    def deleteAddress(self, ownerEmail, spotID):
        self.execute(Address.deleteAddress(ownerEmail, spotID))

    def updateSpotType(self, addressID, spotType):
        self.execute(Address.updateSpotType(addressID, spotType))

    def updateIsCovered(self, addressID, isCovered):
        self.execute(Address.updateIsCovered(addressID, isCovered))

    def updateDescription(self, addressID, description):
        self.execute(Address.updateDescription(addressID, description))

    def viewAddressInfo(self, addressID):
        self.cursor.execute(Address.viewAddressInfo(addressID))
        info = list(self.cursor.fetchall()[0])
        try:
            self.cursor.execute(Address.getPicturePath(addressID))
            picturePath = self.cursor.fetchall()[0][0]
            print picturePath
            info.append(self.objStorageManager.downloadPictureAsB64(picturePath))
        except Exception as e:
            print e
        return info

    def getAddressesOwnedBy(self, userEmail):
        '''
        :type userEmail: str
        :rtype: list
        '''
        self.cursor.execute(Address.searchByOwnerEmailQuery(userEmail))
        return self.cursor.fetchall()

    def getCountForAddress(self, addressID):
        self.cursor.execute(Address.getCount(addressID))
        return self.cursor.fetchall()[0][0]
