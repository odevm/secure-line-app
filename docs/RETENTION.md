# Retention

| Mode | Behavior |
|------|----------|
| `PERSISTENT` | Keep encrypted local rows until delete/uninstall |
| `TIMED` | Wipe after configured TTL (fake-clock tested) |
| `EPHEMERAL` | No durable store; wipe session buffers on exit |

Carrier copies of SMS may remain in the system provider — disclosed in UI banners when SMS lands.
