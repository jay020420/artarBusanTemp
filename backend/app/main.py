from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from typing import List

from .repository import InMemoryRepository
from .schemas import EventSchema, ArtworkSchema

app = FastAPI(title="ArtAR Busan Mock API", version="0.1.0")
repo = InMemoryRepository()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok"}


@app.get("/events", response_model=List[EventSchema])
def get_events() -> List[EventSchema]:
    return [EventSchema(**item) for item in repo.get_events()]


@app.get("/events/{event_id}/artworks", response_model=List[ArtworkSchema])
def get_artworks_by_event(event_id: str) -> List[ArtworkSchema]:
    return [ArtworkSchema(**item) for item in repo.get_artworks_by_event(event_id)]


@app.get("/artworks/{artwork_id}", response_model=ArtworkSchema)
def get_artwork(artwork_id: str) -> ArtworkSchema:
    item = repo.get_artwork(artwork_id)
    if not item:
        raise HTTPException(status_code=404, detail="Artwork not found")
    return ArtworkSchema(**item)
