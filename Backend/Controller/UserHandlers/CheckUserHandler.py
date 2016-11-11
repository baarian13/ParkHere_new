import tornado.web
import tornado.gen
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler
SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'


class CheckUserHandler(AbstractUserHandler):

    @tornado.web.authenticated  # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def get(self):
        email = self.get_argument("email")
        print email
        print self.get_secure_cookie("user")
        if email == self.get_secure_cookie("user"):
            self.write("1")
            print "true"
        else:
            self.write("0")
            print "false"
