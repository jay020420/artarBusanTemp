from pydantic import BaseModel
from typing import Dict


class EventSchema(BaseModel):
    id: str
    name: str
    slug: str
    primaryColor: str
    secondaryColor: str
    logoUrl: str


class VenueSchema(BaseModel):
    id: str
    eventId: str
    name: str
    latitude: float
    longitude: float
    descriptionI18n: Dict[str, str]


class ArtworkSchema(BaseModel):
    id: str
    venueId: str
    markerImageKey: str
    titleI18n: Dict[str, str]
    artist: str
    descriptionI18n: Dict[str, str]
    thumbnailUrl: str
    audioGuideAvailable: bool
