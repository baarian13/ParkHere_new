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
            res = self.db.viewUserInfo(email)
            results = {
                        'first'         : res[0],
                        'last'          : res[1],
                        'isSeeker'      : res[2],
                        'isOwner'       : res[3],
                        'phoneNumber'   : str(res[4]),
                        'email'         : res[5],
                        'rating'        : res[6],
                        'picture'       : res[7]}
            self.write(json.dumps(results))
