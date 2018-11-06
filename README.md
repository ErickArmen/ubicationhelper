# UbicationHelper

This is a demo for a ride sharing app, showcasing using Kotlin in a more idiomatic and expressive way, organization of packages was by done features using Clean Architecture.

The app was divided by features (can be easily modularized if needed), this kind of structure is good in a big enterprise enviroment, 
because you can assign your resources(devs) by features, therefore the developer experience is better; one reason is that you can avoid more merge conflicts, because all they need to solve bugs or implement new functionality is independent of other's code.
In the case of more than one developer is owner of one feature, the advantage still exists in comparison with other kind of architectures 
by the same reason mentioned before, because the changes occurs in a smaller portion of the code.

Tools: RxJava, Dagger2, ViewModel, Paging Library (from AAC) using ItemKeyedDataSource with Firestore and Observables. 

All the keys for services like firebase and google api's was removed for security.
drawables and mipmaps were removed as well, to avoid copyright problems.


<p
align="center">
<a href="https://imgflip.com/gif/2lbtu0"><img src="https://i.imgflip.com/2lbtu0.gif" title="made at imgflip.com"/></a>
</p>
<br>
