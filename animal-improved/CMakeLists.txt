cmake_minimum_required (VERSION 2.6)
project (animals)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=gnu11")

add_executable(animals animals.c animal.c prompt.c)
install (TARGETS animals DESTINATION bin)
