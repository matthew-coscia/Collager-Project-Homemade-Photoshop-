* The CollagerController class is decoupled except for three interfaces. 
* The ModelPrivacy, StatePrivacy, and ViewPrivacy interfaces are an integral part of the CollagerController. 
* The interfaces help to maintain the integrity of the data, as the controller cannot influence anything without calling a direct method from the Model. 
* StatePrivacy allows the controller to change certain things about the CollagerState. But limits access. 
* This is the same for ViewPrivacy. This is a good design choice as it allows the controller to act as a controller, 
* but without the ability to access fields in the Collager project as well as it can be decoupled without bringing half of the project with it.