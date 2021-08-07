# Flickr Search App

## Notes

Architecture: I'm using MVVM with LiveData on the view layer and Rx on the data layer. The conversion from Rx land to Livedata land is done in the view models. The overall architecture is a slight variation of "Clean Architecture" which is why you'd see UseCases being used instead of Repositories. If there was more than one data source (eg: api and local db), this could be handled by adding Repositories as a layer under use cases or the use cases themselves could handle it. Use cases generally help in breaking up business logic that would've gone into a single repository class.

Eg: 
1. Viewmodel -> UseCase (logic but no knowledge on datasources) -> Repository (manages the data sources) -> api or local db
or
2. Viewmodel -> UseCase (logic and handles datasources) -> api or local db

Either method can be selected depending on how much abstraction is needed and how complicated the app is.


## Some compromises due to the lack of time

- I didn't use a local db like Room or Realm.
- Unit tests are not 100% exhaustive. Just added some minimal test cases
