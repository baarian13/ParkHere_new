'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.gen
from DataObjects.User import User
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler
from FunctionalUtils import createSalt, saltPassword

SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'
    
class SignUpHandler(AbstractUserHandler):
    '''
    -Requests are posted here when a user is creating an account.
    -All arguments are submitted as strings.
    '''
    
    @tornado.gen.coroutine
    def post(self):
        '''
        -Each argument is specified in URL format.
        -If an argument is not specified (except picture), then the creation will fail.
        -writes 'success' if everything worked, writes 'partial' if only
            picture could not be added, writes 'failure' if user could not be created
        args:
            email->str
            password->str
            first->str
            last->str
            phone->str without delimiters, length not checked, nor validity nor uniqueness of number
            profilePic->B64 string (if not specified default is used)
            seeker->str--0 for false, 1 for true
            owner->same as seeker
        returns:
            success: 200
            failure: 401
            partial: 206
        '''
        result = SUCCESS
        salt = createSalt()
        args = {'email'          : self.get_argument("email", ""),
                'saltedPassword' : saltPassword(self.get_argument("password", ""), salt),
                'salt'           : salt,
                'firstName'      : self.get_argument("first", ""),
                'lastName'       : self.get_argument("last", ""),
                'phone'          : self.get_argument("phone", ""),
                'isSeeker'       : self.get_argument("seeker", ""),
                'isOwner'        : self.get_argument("owner", "")}
        user = User(**args)
        self.db.insertIntoTable(user)
        userId = self.get_argument("email", "")
        profilePic = self.get_argument("profilePic", "")
        if not userId: result = FAILURE
        elif profilePic: # profile picture support not implemented
            try:
                self.db.submitPicture(userId, profilePic)
            except:
                result = PARTIAL
        self.write(result)
