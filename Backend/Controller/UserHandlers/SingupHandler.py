'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.gen
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler

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
        -writes success if everything worked, writes partial if
            picture could not be added, writes failure if user could not be created
        args:
            email->str
            password->str
            first->str
            last->str
            phone->str without delimiters, length not checked, nor validity nor uniqueness of number
            profilePic->B64 string (if not specified default is used)
            seeker->str--0 for false, 1 for true
            owner->same as seeker
        '''
        result = 'success'
        args = {'email'      : self.get_argument("email", ""),
                'password'   : self.get_argument("password", ""),
                'first'      : self.get_argument("first", ""),
                'last'       : self.get_argument("last", ""),
                'phone'      : self.get_argument("phone", ""),
                'seeker'     : self.get_argument("seeker", ""),
                'owner'      : self.get_argument("owner", "")}
        (userId, success) = self.db.createUser(**args)
        
        profilePic = self.get_argument("profilePic", "") # TODO
        if not success: result = 'failure'
        elif profilePic: # profile picture support not implemented
            (picId, success) = self.db.submitPicture(userId, profilePic)
            
            if success: self.db.addUserPicture(userId, picId)
            else: result = 'partial'
        self.write(result)
