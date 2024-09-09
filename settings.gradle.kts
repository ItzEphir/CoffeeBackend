rootProject.name = "CoffeeBackend"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("data:user")
include("data:password")
include("feature:auth")
include("core:hashing")
findProject(":core:security")?.name = "security"
include("core:jwttoken")
findProject(":core:jwttoken")?.name = "jwttoken"
include("core:token")
findProject(":core:token")?.name = "token"
include("core:log")
findProject(":core:log")?.name = "log"
include("core:config")
findProject(":core:config")?.name = "config"
