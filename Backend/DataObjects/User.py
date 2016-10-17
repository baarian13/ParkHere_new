'''
Created on Oct 17, 2016

@author: henrylevy
'''
from Backend.FunctionalUtils import saltPassword
from Backend.DataObjects.DatabaseObject import DatabaseObject
from Backend.DataObjects.DbField import DBField

class User(DatabaseObject):
    USER_IDENTIFIER = 'email'
    
    DB_INDEX_FIELDS = {'email' : False}
    
    PRIMARY_KEY_NAME = 'email'
    
    PRIMARY_KEY = 'email VARCHAR(50) PRIMARY KEY'
    
    DB_FIELDS = {'firstName'        : DBField('firstName',        'VARCHAR(50)'),
                 'lastName'         : DBField('lastName',         'VARCHAR(50)'),
                 'isSeeker'         : DBField('isSeeker',         'BOOLEAN'),
                 'isOwner'          : DBField('isOwner',          'BOOLEAN'),
                 'saltedPassword'   : DBField('saltedPassword',   'VARCHAR(50)'),
                 'salt'             : DBField('salt',             'VARCHAR(100)'),
                 'email'            : DBField('email',            'VARCHAR(50)'),
                 'profilePictureID' : DBField('profilePictureID', 'INT'),
                 'userID'           : DBField('userID',           'INT')}
    
    def __init__(self, firstName, lastName, isSeeker,
                 isOwner, saltedPassword, salt, email,
                 profilePictureID, userID):
        '''
        :type firstName: str
        :type lastName: str
        :type isSeeker: bool
        :type isOwner: bool
        :type saltedPassword: str
        :type salt: str
        :type email: str
        :type profilePictureID: int
        :type userID: int
        '''
        self.firstName = firstName
        self.lastName = lastName
        self.isSeeker = isSeeker
        self.isOwner = isOwner
        self.saltedPassword = saltedPassword
        self.salt = salt
        self.email = email
        self.profilePictureID = profilePictureID
        self.userID = userID
    
    def checkPassword(self, password): 
        '''
        :type password: str
        :rtype: bool
        '''
        return saltPassword(password, self.salt) == self.saltedPassword
    
