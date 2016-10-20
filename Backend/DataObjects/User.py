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
                        profilePicturePath VARCHAR(200),
                        PRIMARY KEY (email));'''.format(TABLE_NAME)
    
    def __init__(self, firstName, lastName, isSeeker,
                 isOwner, saltedPassword, salt, email,
                 profilePicturePath=None):
        '''
        :type firstName: str
        :type lastName: str
        :type isSeeker: bool
        :type isOwner: bool
        :type saltedPassword: str
        :type salt: str
        :type email: str
        :type profilePicturePath: str
        '''
        self.firstName = firstName
        self.lastName = lastName
        self.isSeeker = isSeeker
        self.isOwner = isOwner
        self.saltedPassword = saltedPassword
        self.salt = salt
        self.email = email
        self.profilePicturePath = profilePicturePath
    
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
        return '''SELECT salt FROM {0}
                    WHERE email = \'{1}\' AND
                    saltedPassword = \'{2}\';'''.format(cls.TABLE_NAME, email, saltedPwd)
    
    @classmethod
    def updateProfilePictureQuery(cls, email, path):
        '''
        :type email: str
        :type path: str
        :rtype: str
        '''
        return '''UPDATE {0} SET profilePicturePath={1} WHERE email={2}'''.format(cls.TABLE_NAME, path, email)
    
    def checkPassword(self, password): 
        '''
        :type password: str
        :rtype: bool
        '''
        return saltPassword(password, self.salt) == self.saltedPassword

    def asInsertStatement(self):
        '''
        :rtype: str
        '''
        params = '''email, firstName, lastName, isSeeker, isOwner, saltedPassword, salt, profilePicturePath'''
        values = '''{0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}'''.format(self.email, self.firstName,
            self.lastName, self.isSeeker, self.isOwner, self.saltedPassword, self.salt, self.profilePicturePath or 0)
        return """INSERT INTO {0} ({1}) VALUES ({2}); """.format(self.TABLE_NAME, params, values)    
