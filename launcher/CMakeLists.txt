cmake_minimum_required(VERSION 3.20)
project(MaazXLauncher LANGUAGES CXX)

add_executable(MaazXLauncher main.cpp)
target_compile_features(MaazXLauncher PRIVATE cxx_std_17)

if(WIN32)
    target_compile_definitions(MaazXLauncher PRIVATE UNICODE _UNICODE WIN32_LEAN_AND_MEAN NOMINMAX)
    target_link_libraries(MaazXLauncher PRIVATE shell32 user32)
    set_target_properties(MaazXLauncher PROPERTIES
        WIN32_EXECUTABLE TRUE
        OUTPUT_NAME "MaazXLauncher"
        CXX_EXTENSIONS OFF
        MSVC_RUNTIME_LIBRARY "MultiThreaded$<$<CONFIG:Debug>:Debug>"
        RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
        RUNTIME_OUTPUT_DIRECTORY_DEBUG "${CMAKE_BINARY_DIR}"
        RUNTIME_OUTPUT_DIRECTORY_RELEASE "${CMAKE_BINARY_DIR}"
        RUNTIME_OUTPUT_DIRECTORY_RELWITHDEBINFO "${CMAKE_BINARY_DIR}"
        RUNTIME_OUTPUT_DIRECTORY_MINSIZEREL "${CMAKE_BINARY_DIR}"
    )
    if(MSVC)
        target_compile_options(MaazXLauncher PRIVATE /W4 /permissive-)
    endif()
else()
    set_target_properties(MaazXLauncher PROPERTIES CXX_EXTENSIONS OFF)
    target_compile_options(MaazXLauncher PRIVATE -Wall -Wextra -Wpedantic)
endif()
