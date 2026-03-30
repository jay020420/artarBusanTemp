from typing import List, Dict, Any
from .seed import EVENTS, ARTWORKS


class InMemoryRepository:
    def get_events(self) -> List[Dict[str, Any]]:
        return EVENTS

    def get_artworks_by_event(self, event_id: str) -> List[Dict[str, Any]]:
        if event_id != "event-busan-2026":
            return []
        return ARTWORKS

    def get_artwork(self, artwork_id: str) -> Dict[str, Any] | None:
        return next((a for a in ARTWORKS if a["id"] == artwork_id), None)
