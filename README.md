# Horoscanner
Horoscope generation app for Android devices. Uses Google's facial detection libraries to scan a photo for a face, hash it in some way, and convert it into a set of outputs:
- A set of lucky numbers
- An animal of the day (with picture)
- Some inspiration quote-type thing
- A colour
- Anything else I can think of that seems interesting

Ideally the current date will be used in the hashing process so that user results change daily, but not between photos.

Wishlist:
- Generate text via some kind of neural network/learning algorithm/markov process
- Integrate facial recognition too (so that the same person will get the same results across different photos
