
import tornado.web
import tornado.gen
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler

SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'


class RateUserHandler(AbstractUserHandler):

    @tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def post(self):
        result = SUCCESS
        email = self.get_argument("email")
        rating = self.get_argument("rating")
        if email and rating:
            try:
                self.db.rateUser(email, rating)
            except:
                result = PARTIAL
        else:
            result = FAILURE

        return result
