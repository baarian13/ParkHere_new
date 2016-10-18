'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado.escape
import tornado.gen
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler

class SigninHandler(AbstractUserHandler):
    '''
    -Requests are posted here when a user is creating an account.
    -All arguments are submitted as strings.
    '''
    
    @tornado.gen.coroutine
    def post(self):
        '''
        -Each argument is specified in URL format.
        -If an argument is not specified  then the creation will fail.
        -writes success if everything writes failure if user could not be authenticated
        -if success token is set, if not, token is not set and client must try again
        args:
            email->str (no checks performed-assumed on client side)
            password->str (no checks performed-assumed on client side)
        '''
        email = self.get_argument("email", "")
        password = self.get_argument("password", "")
        if email and password:
            if self.db.authenticate(email, password):
                self.setCurrentUser(email)
                self.write("authentication successful")
            else:
                self.write("authentication failed")
        else:
            self.write("authentication failed")

    @tornado.gen.coroutine
    def setCurrentUser(self, user):
        '''
        -set secure cookie for future requests
        '''
        if user:
            self.set_secure_cookie("user", tornado.escape.json_encode(user))
        else:
            self.clear_cookie("user")