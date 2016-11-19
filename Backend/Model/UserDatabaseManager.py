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
        print 'submit picture'
        path = 'profilePictures/{0}'.format(email)
        print path
        self.objStorageManager.uploadMedia(path, pictureString)
        print 'succesful media upload'
        self.execute(User.updateProfilePictureQuery(email, path))
        print 'succesful updating profile'

    def submitVerification(self, email, pictureString):
        path = 'verificationPictures/{0}'.format(email)
        self.objStorageManager.uploadMedia(path, pictureString)
        self.execute(User.updateVerificationPictureQuery(email, path))

    def authenticate(self, email, password):
        '''
        :type email: str
        :type password: str
        '''
        print password
        saltedPwd = self.hash(password, self.getSalt(email))
        # if saltedPwd is None:
        #     print "does it get here"
        #     return False
        self.cursor.execute(User.getUserInfoQuery(email, saltedPwd))
        return len(self.cursor.fetchall()) > 0

    def createSalt(self):
        return createSalt()
    
    def getSalt(self, email):
        '''
        :type email: str
        '''
        self.cursor.execute(User.getSaltQuery(email))
        res = self.cursor.fetchall()
        if len(res)> 0:
            print "sending hash"
            return res[0][0]
        else:
            print "sending none"
            return ""
    
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
        info = self.cursor.fetchall()[0]
        
        try:
            self.cursor.execute(User.getPicturePath(email))
            picturePath = self.cursor.fetchall()[0][0]
            info.append(self.objStorageManager.downloadPictureAsString(picturePath))
        except:
            return info, False
        return info, True

    def rateUser(self, email, rating):
        self.cursor.execute(User.getRating(email))
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
        self.execute(User.setRating(email, rating, numReviews))
        self.cursor.execute('''SELECT rating FROM USERS WHERE email = \'rate@user.com\';''')
        print self.cursor.fetchall()[0][0]
