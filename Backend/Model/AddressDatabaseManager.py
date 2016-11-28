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

	'''create address'''

	'''modify address'''

	'''delete address'''

	'''(opt) delete address and related spots'''

	'''get all addresses owned by user with email'''
	def getAddressesOwnedBy(self, userEmail):
		'''
		:type userEmail: str
		:rtype: list of strings
		'''
		self.cursor.execute(Address.searchBy)


	'''get the info of a specific address by address name'''
	def getAddressInfo(self, address):
		'''
		:type address:str
		:rtype: list?
		'''
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

''' this file: Model > SpotDatabaseManager.py '''
''' from Model > SpotDatabaseManager.py:
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
'''
''' from DataObjects > Spot.py:
 @classmethod
    def viewSpotInfo(cls, spotID):
        return ''SELECT address, start, end, spotType, ownerEmail,
        renterEmail, isRecurring, isCovered, cancelationPolicy, description, price, rating FROM SPOTS WHERE ID = {0};''.format(spotID)

'''
''' from Controller > SpotHandlers > ViewSpotHandler.py:
class ViewSpotHandler(AbstractSpotHandler):
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def get(self):
        spotID = self.get_argument("spotID")
        #needs to be finished
        if spotID:
            res = self.db.viewSpotInfo(spotID)
            results = { 'address'           : res[0],
                        'start'             : str(res[1].strftime('%Y-%m-%d %H:%M:%S')),
                        'end'               : str(res[2].strftime('%Y-%m-%d %H:%M:%S')),
                        'spotType'          : res[3],
                        'ownerEmail'        : res[4],
                        'renterEmail'       : res[5],
                        'isRecurring'       : res[6],
                        'isCovered'         : res[7],
                        'cancelationPolicy' : res[8],
                        'description'       : res[9],
                        'price'             : float(res[10]),
                        'rating'           : res[11],
                        'picture'            : res[12]}
            self.write(json.dumps(results))
'''
