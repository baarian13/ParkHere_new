'''
Created on Oct 17, 2016

@author: henrylevy
'''
import tornado, os
from tornado import netutil, process, httpserver
from Controller.AbstractBaseHandler import MainHandler
from Controller.UserHandlers.SinginHandler import SigninHandler
from tornado.ioloop import IOLoop
from Controller.UserHandlers.SingupHandler import SignUpHandler
from Controller.SpotHandlers.PostSpotHandler import PostSpotHandler
from Controller.SpotHandlers.SearchSpotHandler import SearchSpotHandler
settings = {
    "cookie_secret": "ADSFGHARY3457fgSDFHSDFjusdfASDFGH2345h=sdg",
    "login_url": "/signin",
}
data_dir = '/Users/henrylevy/ParkHere/Backend/'
application = tornado.web.Application([
    ("/", MainHandler),
    ("/signin", SigninHandler),
    ("/signup", SignUpHandler),
    ("/post/spot", PostSpotHandler),
    ("/search/spot", SearchSpotHandler)
], ssl_options={
    "certfile": os.path.join(data_dir, "server.crt"),
    "keyfile": os.path.join(data_dir, "server.key"),
}, **settings)

def main():
    sockets = netutil.bind_sockets(8888)
    process.fork_processes(0)
    server = httpserver.HTTPServer(application)
    server.add_sockets(sockets)
    IOLoop.current().start()

if __name__ == "__main__":
    main()