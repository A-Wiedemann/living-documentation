package ch.ifocusit.livingdoc.example.cargo.model;

import ch.ifocusit.livingdoc.annotations.Glossary;
import ch.ifocusit.livingdoc.annotations.RootAggregate;
import ch.ifocusit.livingdoc.example.location.model.Location;
import ch.ifocusit.livingdoc.example.shared.DomainObjectUtils;
import ch.ifocusit.livingdoc.example.shared.Entity;
import ch.ifocusit.livingdoc.example.sharedhandling.model.EmptyHandlingHistory;
import ch.ifocusit.livingdoc.example.sharedhandling.model.HandlingHistory;
import org.apache.commons.lang.Validate;

/**
 * A Cargo. This is the central class in the domain model,
 * and it is the root of the Cargo-Itinerary-Leg-Delivery-RouteSpecification aggregate.
 * <p>
 * A cargo is identified by a unique tracking id, and it always has an origin
 * and a route specification. The life cycle of a cargo begins with the booking procedure,
 * when the tracking id is assigned. During a (short) period of time, between booking
 * and initial routing, the cargo has no itinerary.
 * </p>
 * <p>
 * The booking clerk requests a list of possible routes, matching the route specification,
 * and assigns the cargo to one route. The route to which a cargo is assigned is described
 * by an itinerary.
 * </p>
 * <p>
 * A cargo can be re-routed during transport, on demand of the customer, in which case
 * a new route is specified for the cargo and a new route is requested. The old itinerary,
 * being a value object, is discarded and a new one is attached.
 * </p>
 * <p>
 * It may also happen that a cargo is accidentally misrouted, which should notify the proper
 * personnel and also trigger a re-routing procedure.
 * </p>
 * <p>
 * When a cargo is handled, the status of the delivery changes. Everything about the delivery
 * of the cargo is contained in the Delivery value object, which is replaced whenever a cargo
 * is handled by an asynchronous event triggered by the registration of the handling event.
 * </p>
 * <p>
 * The delivery can also be affected by routing changes, i.e. when a the route specification
 * changes, or the cargo is assigned to a new route. In that case, the delivery update is performed
 * synchronously within the cargo aggregate.
 * </p>
 * <p>
 * The life cycle of a cargo ends when the cargo is claimed by the customer.
 * </p>
 * <p>
 * The cargo aggregate, and the entre domain model, is built to solve the problem
 * of booking and tracking cargo. All important business rules for determining whether
 * or not a cargo is misdirected, what the current status of the cargo is (on board carrier,
 * in port etc), are captured in this aggregate.
 * </p>
 */
@RootAggregate
@Glossary
public class Cargo implements Entity<Cargo> {

    private TrackingId trackingId;
    private Location origin;
    private RouteSpecification routeSpecification;
    private Itinerary itinerary;
    private Delivery delivery;
    // Auto-generated surrogate key
    private Long id;

    public Cargo(final TrackingId trackingId, final RouteSpecification routeSpecification) {
        Validate.notNull(trackingId, "Tracking ID is required");
        Validate.notNull(routeSpecification, "Route specification is required");

        this.trackingId = trackingId;
        // Cargo origin never changes, even if the route specification changes.
        // However, at creation, cargo orgin can be derived from the initial route specification.
        this.origin = routeSpecification.origin();
        this.routeSpecification = routeSpecification;

        this.delivery = Delivery.derivedFrom(this.routeSpecification, this.itinerary, new EmptyHandlingHistory());
    }

    Cargo() {
        // Needed by Hibernate
    }

    /**
     * The tracking id is the identity of this entity, and is unique.
     *
     * @return Tracking id.
     */
    public TrackingId trackingId() {
        return trackingId;
    }

    /**
     * @return Origin location.
     */
    public Location origin() {
        return origin;
    }

    /**
     * @return The delivery. Never null.
     */
    public Delivery delivery() {
        return delivery;
    }

    /**
     * @return The itinerary. Never null.
     */
    public Itinerary itinerary() {
        return DomainObjectUtils.nullSafe(this.itinerary, Itinerary.EMPTY_ITINERARY);
    }

    /**
     * @return The route specification.
     */
    public RouteSpecification routeSpecification() {
        return routeSpecification;
    }

    /**
     * Specifies a new route for this cargo.
     *
     * @param routeSpecification route specification.
     */
    public void specifyNewRoute(final RouteSpecification routeSpecification) {
        Validate.notNull(routeSpecification, "Route specification is required");

        this.routeSpecification = routeSpecification;
        // Handling consistency within the Cargo aggregate synchronously
        this.delivery = delivery.updateOnRouting(this.routeSpecification, this.itinerary);
    }

    /**
     * Attach a new itinerary to this cargo.
     *
     * @param itinerary an itinerary. May not be null.
     */
    public void assignToRoute(final Itinerary itinerary) {
        Validate.notNull(itinerary, "Itinerary is required for assignment");

        this.itinerary = itinerary;
        // Handling consistency within the Cargo aggregate synchronously
        this.delivery = delivery.updateOnRouting(this.routeSpecification, this.itinerary);
    }

    /**
     * Updates all aspects of the cargo aggregate status
     * based on the current route specification, itinerary and handling of the cargo.
     * <p/>
     * When either of those three changes, i.e. when a new route is specified for the cargo,
     * the cargo is assigned to a route or when the cargo is handled, the status must be
     * re-calculated.
     * <p/>
     * {@link RouteSpecification} and {@link Itinerary} are both inside the Cargo
     * aggregate, so changes to them cause the status to be updated <b>synchronously</b>,
     * but changes to the delivery history (when a cargo is handled) cause the status update
     * to happen <b>asynchronously</b> since {@link ch.ifocusit.livingdoc.example.sharedhandling.model.HandlingEvent} is in a different aggregate.
     *
     * @param handlingHistory handling history
     */
    public void deriveDeliveryProgress(final HandlingHistory handlingHistory) {
        // TODO filter events on cargo (must be same as this cargo)

        // Delivery is a value object, so we can simply discard the old one
        // and replace it with a new
        this.delivery = Delivery.derivedFrom(routeSpecification(), itinerary(), handlingHistory);
    }

    @Override
    public boolean sameIdentityAs(final Cargo other) {
        return other != null && trackingId.sameValueAs(other.trackingId);
    }

    /**
     * @param object to compare
     * @return True if they have the same identity
     * @see #sameIdentityAs(Cargo)
     */
    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        final Cargo other = (Cargo) object;
        return sameIdentityAs(other);
    }

    /**
     * @return Hash code of tracking id.
     */
    @Override
    public int hashCode() {
        return trackingId.hashCode();
    }

    @Override
    public String toString() {
        return trackingId.toString();
    }

}