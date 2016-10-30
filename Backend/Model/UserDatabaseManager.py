'''
Created on Jan 3, 2016

@author: henrylevy
'''

from Model.BaseManagers.DatabaseManagerBase import SQLDatabaseManager
from FunctionalUtils import saltPassword, createSalt
from DataObjects.User import User

class SQLUserDatabaseManager(SQLDatabaseManager):
    DB_OBJECT_CLASS = User
    
    def __init__(self, host, user, password, port, db):
        '''
        :type host: str
        :type user: str
        :type password: str
        :type port: int
        :type db: str
        '''
        super(SQLUserDatabaseManager, self).__init__(host, user, password,
                                                 port, db, User)

    def submitPicture(self, email, pictureString):
        '''
        :type email: str
        :type pictureString: str
        '''
        path = 'profilePictures/{0}'.format(email)
        self.objStorageManager.uploadMedia(path, pictureString)
        self.execute(User.updateProfilePictureQuery(email, path))

    def authenticate(self, email, password):
        '''
        :type email: str
        :type password: str
        '''
        saltedPwd = self.hash(password, self.getSalt(email))
        self.cursor.execute(User.getUserInfoQuery(email, saltedPwd))
        return len(self.cursor.fetchall()) > 0

    def createSalt(self):
        return createSalt()
    
    def getSalt(self, email):
        '''
        :type email: str
        '''
        self.cursor.execute(User.getSaltQuery(email))
        return self.cursor.fetchall()[0][0]
    
    def hash(self, password, salt):
        '''
        :type password: str
        :type salt: str
        '''
        return saltPassword(password, salt)

    def changePassword(self, email, saltedPassword):
        self.execute(User.updatePassword(email, saltedPassword))

    def changeFirst(self, email, name):
        self.execute(User.updateFirst(email,name))


    def changeLast(self, email, name):
        self.execute(User.updateLast(email,name))  

    def changePhone(self, email, phone):
        self.execute(User.updatePhone(email,phone))    

    def changeOwner(self, email, isOwner):
        self.execute(User.updateOwner(email, isOwner)) 

    def changeSeeker(self, email, isSeeker):
        self.execute(User.updateSeeker(email,isSeeker))

    def viewUserInfo(self, email):
        self.cursor.execute(User.viewUserInfoQuery(email))
        return self.cursor.fetchall()

    def rateUser(self, email, rating):
        self.cursor.execute(User.getRating(email))
        results = self.cursor.fetchall()
        oldrating = results[0]
        numReviews = results[1]
        rating = oldrating*numReviews + rating
        numReviews += 1
        self.execute(User.setRating(email, rating, numReviews))
