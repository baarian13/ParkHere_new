import tornado.web
import tornado.gen
from Controller.UserHandlers.AbstractUserHandler import AbstractUserHandler

SUCCESS = '200'
FAILURE = '401'
PARTIAL = '206'


class RateUserHandler(AbstractSpotHandler):

    #@tornado.web.authenticated # ensures that user has valid token/is signed in
    @tornado.gen.coroutine
    def post(self):
        result = SUCCESS
        spotId = self.get_argument("spotId")
        rating = self.get_argument("rating")
        if spotId and rating:
            try:
                self.db.rateSpot(spotId, rating)
            except:
                result = PARTIAL
        else:
            result = FAILURE

        return result
