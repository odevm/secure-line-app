cmake_minimum_required(VERSION 3.22.1)
project(secureline_modem C)

add_library(secureline_modem SHARED
    src/modem.c
    src/fec.c
    src/fsk.c
    src/codec2_stub.c
    src/jni_bridge.c
)

target_include_directories(secureline_modem PUBLIC include)
target_include_directories(secureline_modem PRIVATE src)

find_library(log-lib log)
if (log-lib)
    target_link_libraries(secureline_modem ${log-lib})
endif()
