'''
Created on Oct 17, 2016

@author: henrylevy
'''
from FunctionalUtils import saltPassword
from DataObjects.DatabaseObject import DatabaseObject

class User(DatabaseObject):
    TABLE_NAME = "USERS"
    TABLE_CREATE_STATEMENT = '''CREATE TABLE IF NOT EXISTS {0}(
                        email VARCHAR(100) NOT NULL,
                        firstName VARCHAR(50) NOT NULL,
                        lastName VARCHAR(50) NOT NULL,
                        isSeeker BOOL NOT NULL,
                        isOwner BOOL NOT NULL,
                        saltedPassword VARCHAR(200) NOT NULL,
                        salt VARCHAR(100) NOT NULL,
                        phone VARCHAR(100) NOT NULL,
                        numReviews INT NOT NULL,
                        rating FLOAT NOT NULL,
                        profilePicturePath VARCHAR(200),
                        PRIMARY KEY (email));'''.format(TABLE_NAME)
    
    def __init__(self, firstName, lastName, isSeeker,
                 isOwner, saltedPassword, salt, email,
                 phone, profilePicturePath=None, 
                 numReviews=0, rating=0):
        '''
        :type firstName: str
        :type lastName: str
        :type isSeeker: bool
        :type isOwner: bool
        :type saltedPassword: str
        :type salt: str
        :type email: str
        :type phone: str
        :type profilePicturePath: str
        :type numReviews: int
        :type rating: int
        '''
        self.firstName          = firstName
        self.lastName           = lastName
        self.isSeeker           = isSeeker
        self.isOwner            = isOwner
        self.saltedPassword     = saltedPassword
        self.phone              = phone
        self.salt               = salt
        self.email              = email
        self.profilePicturePath = profilePicturePath or ''
        self.numReviews         = numReviews
        self.rating             = rating
        self.data = {'firstName'          : str(firstName),
                     'lastName'           : str(lastName),
                     'isSeeker'           : bool(isSeeker),
                     'isOwner'            : bool(isOwner),
                     'saltedPassword'     : saltedPassword,
                     'phone'              : str(phone),
                     'salt'               : salt,
                     'email'              : str(email),
                     'profilePicturePath' : profilePicturePath,
                     'numReviews'         : numReviews,
                     'rating'             : rating}
        if not profilePicturePath:
            self.data.pop('profilePicturePath')
    
    @classmethod
    def getSaltQuery(cls, email):
        '''
        :type email: str
        '''
        return '''SELECT salt FROM {0}
                    where email = \'{1}\';'''.format(cls.TABLE_NAME, email)

    @classmethod
    def getUserInfoQuery(cls, email, saltedPwd):
        '''
        :type email: str
        :type saltedPwd: str
        '''
        return '''SELECT * FROM {0}
                    WHERE email = \'{1}\' AND
                    saltedPassword = \'{2}\';'''.format(cls.TABLE_NAME, email, saltedPwd)
    
    @classmethod
    def updateProfilePictureQuery(cls, email, path):
        '''
        :type email: str
        :type path: str
        :rtype: str
        '''
        return '''UPDATE {0} SET profilePicturePath=\'{1}\' WHERE email=\'{2}\''''.format(cls.TABLE_NAME, path, email)

    @classmethod
    def updatePassword(cls, email, saltedPassword):
        return '''UPDATE {0} SET saltedPassword=\'{1}\' WHERE email=\'{2}\''''.format(cls.TABLE_NAME, saltedPassword, email)

    @classmethod
    def updateFirst(cls, email, name):
        return '''UPDATE {0} SET firstName=\'{1}\' WHERE email=\'{2}\''''.format(cls.TABLE_NAME, name, email)

    @classmethod
    def updateLast(cls, email, name):
        return '''UPDATE {0} SET lastName=\'{1}\' WHERE email=\'{2}\''''.format(cls.TABLE_NAME, name, email)

    @classmethod
    def updatePhone(cls, email, phone):
        return '''UPDATE {0} SET phone=\'{1}\' WHERE email=\'{2}\''''.format(cls.TABLE_NAME, name, email)

    @classmethod
    def checkPassword(self, password): 
        '''
        :type password: str
        :rtype: bool
        '''
        return saltPassword(password, self.salt) == self.saltedPassword

    @classmethod
    def viewUserInfoQuery(cls, email):
        return '''SELECT email, rating, phoneNumber, FROM {0}
                    WHERE email = \'{1}\';'''.format(cls.TABLE_NAME, email)

    @classmethod
    def getRating(cls, email):
        return '''SELECT rating, numReviews, FROM {0}
                    WHERE email = \'{1}\';'''.format(cls.TABLE_NAME, email)

    @classmethod
    def setRating(cls, email, rating, numReviews):
            return '''UPDATE {0} SET rating=\'{1}\' AND numReviews = \'{2}\' WHERE email=\'{3}\''''.format(cls.TABLE_NAME, rating, numReviews, email)
