import tornado.web
import tornado.gen
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler
import json
SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'

class ViewUserProfileHandler(AbstractUserHandler):
    @tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def get(self):
        email = self.get_argument("email")
        if email:
            results = [{'email'         : res[0],
                        'rating'        : res[1],
                        'phoneNumber'   : str(res[2])}
                for res in self.db.viewUserInfo(email)]
            self.write(json.dumps(results))
