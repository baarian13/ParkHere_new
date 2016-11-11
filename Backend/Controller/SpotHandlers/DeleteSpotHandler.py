import tornado.gen
from Controller.SpotHandlers.AbstractSpotHandler import AbstractSpotHandler

SUCCESS = '200'
FAILURE = '401'


class DeleteSpotHandler(AbstractSpotHandler):
    '''
    -Requests are posted here when a user is creating an account.
    -All arguments are submitted as strings.
    '''
    @tornado.web.authenticated
    @tornado.gen.coroutine
    def post(self):
        result = SUCCESS
        try:
            ownerEmail = self.get_secure_cookie("user")
            spotID = self.get_argument("spotID", "")
            self.db.deleteSpot(ownerEmail, spotID)
        except:
            result = FAILURE

        self.write(result)
