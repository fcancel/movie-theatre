# Introduction

This application has been redesigned to keep it easily extensible, by making classes with single responsibilities, and open for extension, as this is an initial approach, and it is expected to further evolve and have more features.

An example for this would be, what if we have a different Schedule per day? This has been acknowledged and architectured to be able to evolve into that.

Same with the sequence of showing, we could need to modify some dates Schedule, and having to manually adjust the order would not be ideal.

## Further Improvements

As the application will likely be a web-API, it has been oriented in Services that have the most uses, as is the Booking service, the Schedule Service and the Schedule Printing Service.

If the application goes this course, a good framework for this would be Spring Framework, and we could use it's Inversion of Control to inject different versions of the objects. This has been taken into account, that is why objects have constructors where Spring can do it's Autowiring
