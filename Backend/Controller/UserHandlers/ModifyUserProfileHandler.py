'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.web
import tornado.gen
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler
from FunctionalUtils import createSalt, saltPassword


SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'

class ModifyUserProfileHandler(AbstractUserHandler):
    @tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def get(self):
        pass

    #@tornado.web.authenticated # ensures that user has valid token/is signed in
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
        email = self.get_argument("email","")
        password = self.get_argument("password","")
        if password: 
            salt = createSalt()
            saltedPassword = saltPassword(password,salt)
            try:
                self.db.changePassword(email,saltedPassword)
            except:
                result = PARTIAL
        firstName = self.get_argument("first","")
        if firstName:
            try:
                self.db.changeFirst(email ,firstName)
            except:
                result = PARTIAL
        lastName = self.get_argument("last","")
        if lastName:
            try:
                self.db.changeLast(email ,lastName)
            except:
                result = PARTIAL
        phone = self.get_argument("phone","")
        if phone:
            try:
                self.db.changePhone(email ,phone)
            except:
                result = PARTIAL
        isSeeker = self.get_argument("isSeeker","")
        if isSeeker:
            try:
                self.db.changeSeeker(email , isSeeker)
            except:
                result = PARTIAL
        isOwner = self.get_argument("isOwner","")
        if isOwner:
            try:
                self.db.changeOwner(email , isOwner)
            except:
                result = PARTIAL
        profilePic = self.get_argument("profilePic", "")
        if profilePic:
            try:
                self.db.submitPicture(email , profilePic)
            except:
                result = PARTIAL
        self.write(result)
