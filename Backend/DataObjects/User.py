'''
Created on Oct 17, 2016

@author: henrylevy
'''
from FunctionalUtils import saltPassword
from DataObjects.DatabaseObject import DatabaseObject

class User(DatabaseObject):
    TABLE_NAME = "USERS"
    TABLE_CREATE_STATEMENT = '''CREATE TABLE IF NOT EXISTS {0}(
                        ID INT AUTO_INCREMENT,
                        firstName VARCHAR(50) NOT NULL,
                        lastName VARCHAR(50) NOT NULL,
                        isSeeker BOOL NOT NULL,
                        isOwner BOOL NOT NULL,
                        saltedPassword VARCHAR(200) NOT NULL,
                        salt VARCHAR(100) NOT NULL,
                        email VARCHAR(100) NOT NULL,
                        profilePictureID INT,
                        FOREIGN KEY (profilePictureID) REFERENCES PICTURES(ID),
                        PRIMARY KEY (ID));'''.format(TABLE_NAME)
    
    
    
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
    
    @classmethod
    def getSaltQuery(cls, email):
        return '''select salt from {0}
                    where email = \'{1}\';'''.format(cls.TABLE_NAME, email)

    @classmethod
    def getUserInfoQuery(cls, email, saltedPwd):
        return '''select salt from {0}
                    where email = \'{1}\' and
                    saltedPassword = \'{2}\';'''.format(cls.TABLE_NAME, email, saltedPwd)
    
    def checkPassword(self, password): 
        '''
        :type password: str
        :rtype: bool
        '''
        return saltPassword(password, self.salt) == self.saltedPassword

    def asInsertStatement(self):
        startDate = "{0}-{1}-{2}".format(self.startDate.year,
                                         self.startDate.month,
                                         self.startDate.day)
        endDate = "{0}-{1}-{2}".format(self.endDate.year,
                                       self.endDate.month,
                                       self.endDate.day)
        return """INSERT INTO {0} 
        (renterID, ownerID, price, address, spotType, isBooked, startDate, endDate) 
        VALUES ({1}, {2}, {3}, {4}, {5}, {6}, {7}, {8});
        """.format(self.TABLE_NAME, self.renterUserID, self.ownerID, str(self.price),
                   self.address, self.spotType, self.isBooked, startDate, endDate)    
