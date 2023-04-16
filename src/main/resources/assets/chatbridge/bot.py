import asyncio
import base64
import socket
import sys
import time
import threading
import discord
from discord.ext import commands

ip = sys.argv[1]
port = int(sys.argv[2])
token = sys.argv[3]
channel_id = int(sys.argv[4])

def debug(*args):
    print("[ChatBridge]", *args, flush=True)

class Client:
    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.setblocking(True)

    def connect(self):
        self.socket.connect((self.host, self.port))

    def send(self, message):
        self.socket.sendall(message.encode("utf-8") + b"\n")
        # debug(f"Relayed message to server: {message}")

    def receive(self):
        # try:
        #     return self.socket.recv(1024).decode("utf-8")
        # except:
        #     return None
        return self.socket.makefile().readline().strip()

    def close(self):
        self.socket.close()

intents = discord.Intents.all()
bot = commands.Bot(command_prefix="!", intents=intents)

relay = Client(ip, port)
# loop until the server is ready
while True:
    try:
        relay.connect()
        break
    except:
        debug("Failed to connect to server, retrying in 1 second...")
        time.sleep(1)
        pass

def listen_for_messages():
    while True:
        encoded_message = relay.receive()
        if encoded_message:
            message = base64.b64decode(encoded_message.encode("utf-8")).decode("utf-8")
            # debug(f"Relaying message to Discord: {message}")
            channel = bot.get_channel(channel_id)
            asyncio.run_coroutine_threadsafe(channel.send(message), bot.loop)

thread = threading.Thread(target=listen_for_messages)
thread.start()

@bot.event
async def on_ready():
    debug(f"Logged in as {bot.user}")

@bot.event
async def on_message(message):
    if message.author == bot.user:
        return
    if message.channel.id == channel_id:
        # debug(f"Received Discord message: <{message.author}>: {message.content}")
        encoded_author = base64.b64encode(str(message.author).encode("utf-8")).decode("utf-8")
        encoded_message = base64.b64encode(message.content.encode("utf-8")).decode("utf-8")
        relay.send(fr"{encoded_author},{encoded_message}")

async def main():
    await bot.start(token)

asyncio.run(main())
